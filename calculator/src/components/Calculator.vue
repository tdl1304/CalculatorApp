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
  <div class="log">
    <div v-for="log in logs" :key="log">{{ log }}</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      lastOperatorClicked: "",
      logs: ["LOG"],
      previous: "",
      current: "123",
      operator: null,
      operatorClicked: false,
    };
  },
  methods: {
    clear() {
      this.current = "";
    },
    sign() {
      this.current =
        this.current.charAt(0) === "-"
          ? this.current.slice(1)
          : `-${this.current}`;
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
    setPrevious() {
      this.previous = this.current;
      this.operatorClicked = true;
    },
    divide() {
      this.lastOperatorClicked = "÷";
      this.operator = (a, b) => a / b;
      this.setPrevious();
    },
    times() {
      this.lastOperatorClicked = "*";
      this.operator = (a, b) => a * b;
      this.setPrevious();
    },
    minus() {
      this.lastOperatorClicked = "-";
      this.operator = (a, b) => a - b;
      this.setPrevious();
    },
    add() {
      this.lastOperatorClicked = "+";
      this.operator = (a, b) => a + b;
      this.setPrevious();
    },
    equal() {
      let result = `${this.operator(
        parseFloat(this.previous),
        parseFloat(this.current)
      )}`;
      this.logs.push(
        `${this.previous}${this.lastOperatorClicked}${
          this.current
        }${"="}${result}`
      );
      this.current = result;
      this.previous = null;
    },
    async getData(url = "") {
      const response = await fetch(url, {
        method: "GET", // *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, *cors, same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "same-origin", // include, *same-origin, omit
        redirect: "follow", // manual, *follow, error
        referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url // body data type must match "Content-Type" header
      });
      return response; // parses JSON response into native JavaScript objects
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
