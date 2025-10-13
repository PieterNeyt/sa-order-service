document.addEventListener("DOMContentLoaded", async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const orderId = urlParams.get("orderId");

    if (!orderId) {
        alert("Geen orderId gevonden in de URL.");
        return;
    }

    try {
        // Haal orderdetails op van je backend
        const cartResponse = await fetch(`http://localhost:9090/api/order/${orderId}/shoppingCart/`, {
            method: "GET"
        });
        if (!cartResponse.ok) {
            throw new Error(`Fout bij ophalen van winkelmand (status: ${cartResponse.status})`);
        }

        const shoppingCart = await cartResponse.json();

        // Maak checkoutRequest-object aan dat het restaurant verwacht
        const checkoutRequest = {
            orderId: orderId,
            orderStatus: shoppingCart.orderStatus,
            items: shoppingCart.map((item: any) => ({
                dishId: item.dishId,
                name: item.name,
                preparationTime: item.preparationTime,
                price: item.price
            }))
        };

        // Vul productenlijst
        const itemsList = document.getElementById("order-items");
        if (itemsList) {
            itemsList.innerHTML = ""; // leegmaken
            checkoutRequest.items.forEach((item: any) => {
                const li = document.createElement("li");
                li.textContent = `${item.name} — €${item.price.toFixed(2)}`;
                itemsList.appendChild(li);
            });
        }

        // Vul totaalprijs
        const totalPriceElement = document.getElementById("total-price");
        if (totalPriceElement) {
            const total = checkoutRequest.items.reduce((sum: number, item: any) => sum + item.price, 0);
            totalPriceElement.textContent = total.toFixed(2);
        }

        // Vul status
        const statusElement = document.getElementById("order-status");
        if (statusElement) statusElement.textContent = checkoutRequest.orderStatus || "In behandeling";

    } catch (error) {
        console.error("Fout bij laden van trackingdata:", error);
        alert("Er is een fout opgetreden bij het laden van de orderinformatie.");
    }
});
