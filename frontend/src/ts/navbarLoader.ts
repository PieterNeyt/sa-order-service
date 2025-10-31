export function initNavbar(): void {
    initLoginButton();
}

function initLoginButton(): void {
    const loginBtn = document.getElementById('login-btn');
    if (loginBtn) {
        loginBtn.addEventListener('click', handleLoginLogout);
        updateLoginButton();
    }
}

function handleLoginLogout(): void {
    if (sessionStorage.getItem('client_id')) {
        handleLogout();
    } else {
        handleLogin();
    }
}

async function handleLogin(): Promise<void> {
    try {
        const response = await fetch("http://localhost:9090/api/order/login", {
            method: "GET",
            headers: { "Content-Type": "application/json" }
        });

        if (!response.ok) {
            alert("Login mislukt!");
            return;
        }

        const user = await response.json();

        sessionStorage.setItem("client_id", user.id);

        console.log("Ingelogd als:", user);
        alert(`Welkom ${user.firstName}!`);
        updateLoginButton();
    } catch (error) {
        console.error("Fout bij login:", error);
        alert("Er is een fout opgetreden bij het inloggen.");
    }
}

function handleLogout(): void {
    sessionStorage.removeItem('client_id');

    console.log('Succesvol uitgelogd!');
    alert('Succesvol uitgelogd!');
    updateLoginButton();
}

function updateLoginButton(): void {
    const loginBtn = document.getElementById('login-btn');
    if (!loginBtn) return;

    const isLoggedIn = sessionStorage.getItem('client_id');

    loginBtn.textContent = isLoggedIn
        ? `Uitloggen`
        : 'Inloggen';
}
