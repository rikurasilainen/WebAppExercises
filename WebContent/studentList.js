
const main = () => {
	fetch("http://localhost:8080/WebAppExercises/students")
		.then(response => response.json())
		.then(studentList => createStudentsTable(studentList))
		.catch(error => console.log(error))
}

const createStudentsTable = (studentList) => {
	const table = document.querySelector("table");
	const students = Object.keys(studentList[0]);

	generateHead(table, students);
	generateRows(table, studentList);
}

const generateHead = (table, students) => {
	const tableHead = table.createTHead();
	const row = tableHead.insertRow();

	for (const key of students) {
		let th = document.createElement("th");
		let text = document.createTextNode(key);
		th.appendChild(text);
		row.appendChild(th);
	}
}

const generateRows = (table, studentList) => {
	for (const student of studentList) {
		let row = table.insertRow();

		for (key in student) {
			let cell = row.insertCell();
			let text = document.createTextNode(student[key]);
			cell.appendChild(text);
		}
	}
}

main();

// I also wrote the script with functions just in case that was required, both work all the same.

/*
function main() {
	fetch("http://localhost:8080/WebAppExercises/students")
		.then(response => response.json())
		.then(studentList => createStudentsTable(studentList))
}

function createStudentsTable(studentList) {
	const table = document.querySelector("table");
	const students = Object.keys(studentList[0]);

	generateHead(table, students);
	generateRows(table, studentList);
}

function generateHead(table, students) {
	const tableHead = table.createTHead();
	const row = tableHead.insertRow();

	for (const key of students) {
		let th = document.createElement("th");
		let text = document.createTextNode(key);
		th.appendChild(text);
		row.appendChild(th);
	}
}

function generateRows(table, studentList) {
	for (const student of studentList) {
		let row = table.insertRow();

		for (key in student) {
			let cell = row.insertCell();
			let text = document.createTextNode(student[key]);
			cell.appendChild(text);
		}
	}
}

main();

*/