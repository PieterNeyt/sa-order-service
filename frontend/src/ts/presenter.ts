import type {testInterface} from "./domain/Test.ts";

const testTable = document.getElementById("tests") as HTMLTableElement
const SERVER_URL = "http://localhost:8080"

export async function fetchAllTest():Promise<testInterface[]>{
    const response = await fetch(SERVER_URL + "/api/tests/")

    if(response.ok){
        return response.json()
    }
    return Promise.resolve([])
}

function showTests(test: testInterface[]) {
    testTable.innerHTML = test.map(t => `
       <tr>
                <td>${t.testId}</td>
                <td>${t.testName}</td>
                      <td>${t.testDescription}</td>
                  </tr>
    `).join()
}

export async function getAndShowAllTests(){
    showTests(await fetchAllTest());
}