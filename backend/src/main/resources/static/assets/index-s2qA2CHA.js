(function(){const r=document.createElement("link").relList;if(r&&r.supports&&r.supports("modulepreload"))return;for(const e of document.querySelectorAll('link[rel="modulepreload"]'))n(e);new MutationObserver(e=>{for(const t of e)if(t.type==="childList")for(const s of t.addedNodes)s.tagName==="LINK"&&s.rel==="modulepreload"&&n(s)}).observe(document,{childList:!0,subtree:!0});function i(e){const t={};return e.integrity&&(t.integrity=e.integrity),e.referrerPolicy&&(t.referrerPolicy=e.referrerPolicy),e.crossOrigin==="use-credentials"?t.credentials="include":e.crossOrigin==="anonymous"?t.credentials="omit":t.credentials="same-origin",t}function n(e){if(e.ep)return;e.ep=!0;const t=i(e);fetch(e.href,t)}})();const c=document.getElementById("tests"),l="http://localhost:8080";async function d(){const o=await fetch(l+"/api/tests/");return o.ok?o.json():Promise.resolve([])}function u(o){c.innerHTML=o.map(r=>`
       <tr>
                <td>${r.testId}</td>
                <td>${r.testName}</td>
                      <td>${r.testDescription}</td>
                  </tr>
    `).join()}async function f(){u(await d())}f();
