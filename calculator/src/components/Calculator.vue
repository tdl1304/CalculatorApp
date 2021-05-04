<template>
  <div class="calculator">
    <div class="display">{{ current || "0" }}</div>
    <div @click="clear" class="btn">C</div>
    <div @click="sign" class="btn">+/-</div>
    <div @click="percent" class="btn">%</div>
    <div @click="divide" class="btn operator">÷</div>
    <div @click="append('7')" class="btn">7</div>
    <div @click="append('8')" class="btn">8</div>
    <div @click="append('9')" class="btn">9</div>
    <div @click="times" class="btn operator">x</div>
    <div @click="append('4')" class="btn">4</div>
    <div @click="append('5')" class="btn">5</div>
    <div @click="append('6')" class="btn">6</div>
    <div @click="minus" class="btn operator">-</div>
    <div @click="append('1')" class="btn">1</div>
    <div @click="append('2')" class="btn">2</div>
    <div @click="append('3')" class="btn">3</div>
    <div @click="add" class="btn operator">+</div>
    <div @click="append('0')" class="btn zero">0</div>
    <div @click="dot" class="btn">.</div>
    <div @click="equal" class="btn operator">=</div>
  </div>
  <button style="padding:0.3em 1em; margin-top:1em;" @click="logs=['LOG']">Clear log</button>
  <div class="log">
    <div v-for="log in logs" :key="log">{{ log }}</div>
  </div>
</template>

<script>
const API_ENDPOINT = "http://localhost:8081/api"
export default {
  data() {
    return {
      lastOperatorClicked: "",
      logs: ["LOG"],
      previous: "",
      current: "",
      operatorClicked: false,
    };
  },
  methods: {
    clear() {
      this.previous = "";
      this.current = "";
    },
    sign() {
      if(this.current !== "") {
        this.current =
            this.current.charAt(0) === "-"
                ? this.current.slice(1)
                : `-${this.current}`;
      }
    },
    percent() {
      this.current = `${parseFloat(this.current) / 100}`;
    },
    append(number) {
      if (this.operatorClicked) {
        this.current = "";
        this.operatorClicked = false;
      }
      this.current = `${this.current}${number}`;
    },
    dot() {
      if (this.current.indexOf(".") === -1) {
        this.append(".");
      }
    },
    divide() {
      this.calculate("/")
    },
    times() {
      this.calculate("*")
    },
    minus() {
      this.calculate("-")
    },
    add() {
      this.calculate("+")
    },
    calculate(operator) {
      if(!this.operatorClicked) {
        if (this.previous !== "") {
          this.getData(API_ENDPOINT + "/calculate", {regnestykke: `${this.previous}${this.current}`})
              .then(data => data.json())
              .then(data => {
                this.logs.push(`${this.previous}${this.current} = ${data.toString()}`)
                this.current = data.toString();
                this.previous = data.toString() + operator;
              })
        }
        this.previous = this.current + operator;
        this.operatorClicked = true;
      }
    },
    equal() {
      if(this.previous !== "") {

        this.getData(API_ENDPOINT + "/calculate", {regnestykke: `${this.previous}${this.current}`})
            .then(data => data.json())
            .then(data => {
              this.logs.push(`${this.previous}${this.current} = ${data.toString()}`)
              this.current = data.toString();
              this.previous = "";
            })
      }
    },
    async getData(url = "", data) {
      return await fetch(url, {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, *cors, same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "same-origin", // include, *same-origin, omit
        redirect: "follow", // manual, *follow, error
        referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      });
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.calculator {
  margin: 0 auto;
  width: 50%;
  font-size: 40px;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-auto-rows: minmax(50px, auto);
}

.display {
  text-align: right;
  grid-column: 1 / 5;
  background: #333;
  color: white;
  border: 1px double #333;
  padding-right: 1rem;
}

.zero {
  grid-column: 1 / 3;
}

.btn {
  text-align: center;
  background-color: #f2f2f2;
  border: 1px solid #999;
}

.btn:hover {
  background-color: whitesmoke;
}

.operator {
  background-color: orange;
  color: white;
}

.operator:hover {
  background-color: orangered;
}

.log {
  font-size: 20px;
  padding: 0 1rem;
  margin-top: 1rem;
  background-color: #eee;
  border: 1px solid #999;
  display: grid;
  grid-auto-rows: minmax(25px, auto);
}
</style>
