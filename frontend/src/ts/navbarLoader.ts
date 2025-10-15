export function initNavbar(): void {
    initLoginButton();
}

function initLoginButton(): void {
    const loginBtn = document.getElementById('login-btn');
    if (loginBtn) {
        loginBtn.addEventListener('click', handleLoginLogout);
        updateLoginButton(); // zet meteen de juiste status bij refresh
    }
}

function handleLoginLogout(): void {
    if (sessionStorage.getItem('jwt_token')) {
        handleLogout();
    } else {
        handleLogin();
    }
}

async function handleLogin(): Promise<void> {
    try {
        const params = new URLSearchParams({
            client_id: 'backend-client',
          //  client_secret: 'NHhC580gogEpJ5K9fqsiyLiqF4VpqTKE',//Pieter
            client_secret:'pBAtElyMTHBp0IBEwO8G8h7bt6Jb4khZ',//Hugo
            username: 'client',
            password: 'password',
            grant_type: 'password',
            scope: 'openid'
        });

        const response = await fetch('http://localhost:8180/realms/keepdishesgoing/protocol/openid-connect/token', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params.toString()
        });

        if (!response.ok) throw new Error('Login failed');

        const data = await response.json();
        sessionStorage.setItem('jwt_token', data.access_token);

        console.log('Succesvol ingelogd!');
        console.log('JWT Token:', data.access_token);

        alert('Login successful!');
        updateLoginButton();
    } catch (error) {
        console.error('Login error:', error);
        alert('Login failed');
    }
}

function handleLogout(): void {
    const token = sessionStorage.getItem('jwt_token');
    sessionStorage.removeItem('jwt_token');

    console.log('Succesvol uitgelogd!');
    console.log('Verwijderde JWT Token:', token);

    alert('Succesvol uitgelogd!');
    updateLoginButton();
}

function updateLoginButton(): void {
    const loginBtn = document.getElementById('login-btn');
    if (!loginBtn) return;

    if (sessionStorage.getItem('jwt_token')) {
        loginBtn.textContent = 'Uitloggen';
    } else {
        loginBtn.textContent = 'Inloggen';
    }
}