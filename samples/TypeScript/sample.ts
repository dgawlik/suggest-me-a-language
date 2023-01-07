/*
    == Suggest me a language, 2023 ==
*/


enum Profession {
    IT,
    Sales,
    Manager,
    Finance
}


interface Person {
    firstName: string,
    lastName: string,
    age: number,
    profession: Profession
}


function aList(): Person[] {
    return [
        {firstName: "Theodore", lastName: "Roosvelt", age: 67, profession: Profession.Finance},
        {firstName: "John", lastName: "Graham", age: 34, profession: Profession.Finance},
        {firstName: "Ada", lastName: "Lovelace", age: 45, profession: Profession.Sales},
        {firstName: "Elizabeth", lastName: "Taylor", age: 20, profession: Profession.Finance}
    ]
}

let sortedPeople = aList()
                    .filter( (x:Person) => x.profession == Profession.Finance)
                    .sort( (l:Person, r:Person) => l.age - r.age);

console.log(sortedPeople);

// Running => npx ts-node sample.ts
