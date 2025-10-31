export async function startPaymentPolling(orderId: string) {
    const progressEl = document.getElementById('progress');
    const maxChecks = 30;
    const checkInterval = 2000;
    const timeout = maxChecks * checkInterval;

    function updateProgress(message: string) {
        if (progressEl) progressEl.textContent = message;
        console.log(message);
    }

    async function checkPaymentStatus(): Promise<boolean> {
        try {
            const response = await fetch(`http://localhost:9090/api/order/${orderId}/confirm`, {
                method: 'POST',
            });

            if (response.ok) {
                updateProgress("Betaling bevestigd!");
                window.location.href = `ordertracking.html?orderId=${orderId}`;
                return true; // Stop polling
            } else if (response.status === 400 || response.status === 404) {
                updateProgress(" Betaling nog niet bevestigd, opnieuw proberen...");
                return false;
            } else {
                throw new Error(`Server returned status ${response.status}`);
            }
        } catch (err: any) {
            throw new Error(err.message || "Onbekende fout bij checkPaymentStatus");
        }
    }

    function showError(message: string) {
        const container = document.querySelector('.container');
        if (container) {
            container.innerHTML = `
                <h2> Fout</h2>
                <p>${message}</p>
                <button onclick="window.location.href='index.html'">Terug naar home</button>
            `;
        }
    }

    const pollingInterval = setInterval(async () => {
        try {
            const done = await checkPaymentStatus();
            if (done) clearInterval(pollingInterval);
        } catch (error: any) {
            clearInterval(pollingInterval);
            showError(`Betaling kon niet worden bevestigd: ${error.message}`);
        }
    }, checkInterval);

    setTimeout(() => {
        clearInterval(pollingInterval);
        showError("Betaling controle timeout. Controleer later of de betaling is gelukt.");
    }, timeout);
}

// Automatisch starten bij laden van de pagina
const urlParams = new URLSearchParams(window.location.search);
const orderId = urlParams.get('orderId');

if (orderId) {
    startPaymentPolling(orderId);
} else {
    const progressEl = document.getElementById('progress');
    if (progressEl) progressEl.textContent = "Geen orderId opgegeven!";
}
