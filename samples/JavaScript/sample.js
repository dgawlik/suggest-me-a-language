/*
    == Suggest me a language, 2023 ==
*/


// destructuring assignments
let [x, y] = [3, 4];

// spread operator
// function definition
function printCoordinates(...coordinates) {
    console.log(coordinates);
}

let z = 1;
let funcs = [];

//for
for (let i=0;i < 5;i++) {
    //lambda
    //lexical closure
    funcs.push((x) => z + x + i);
}

//filter, map, reduce
let result = funcs
                .map(fn => fn(1))
                .filter(x => x % 2 == 0)
                .reduce((prev, curr) => prev + curr);

console.log(result);


//prototyping
function Animal(name) {
    this.name = name;
}

function Dog(name) {
    Animal.call(this, name);
}

Dog.prototype = Object.create(Animal.prototype);
Dog.prototype.say = function() {
    console.log(this.name);
    console.log("Bark!");
}
Dog.prototype.contructor = Dog;

function Duck(name) {
    Animal.call(this, name);
}

Duck.prototype = Object.create(Animal.prototype);
Duck.prototype.say = function() {
    console.log(this.name);
    console.log("Quack!");
}
Duck.prototype.constructor = Duck;


[
    new Duck("Bob"),
    new Dog("Peter")
].forEach(it => it.say());


/* Running => node sample.js */

