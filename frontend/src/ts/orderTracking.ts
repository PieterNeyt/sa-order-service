import type {Order} from "./domain/OrderLine.ts";

async function getOrderTracking(orderId: string): Promise<Order> {
    const jwtToken = sessionStorage.getItem("jwt_token");

    if (!jwtToken) {
        throw new Error('Je moet ingelogd zijn');
    }

    const response = await fetch(`http://localhost:9090/api/order/${orderId}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${jwtToken}`,
            "Content-Type": "application/json"
        }
    });

    if (!response.ok) {
        throw new Error(`Fout bij ophalen van order: ${response.status}`);
    }

    return await response.json();
}

function formatPrice(price: number): string {
    return new Intl.NumberFormat('nl-BE', {
        style: 'currency',
        currency: 'EUR'
    }).format(price);
}

function calculateTotal(items: { price: number; quantity: number }[]): number {
    return items.reduce((total, item) => total + (item.price * item.quantity), 0);
}

async function loadOrderTracking() {
    const urlParams = new URLSearchParams(window.location.search);
    const orderId = urlParams.get("orderId");

    const loadingElement = document.getElementById('loading');
    const errorElement = document.getElementById('error');
    const contentElement = document.getElementById('order-content');

    if (!orderId) {
        if (loadingElement) loadingElement.style.display = 'none';
        if (errorElement) {
            errorElement.style.display = 'block';
            errorElement.innerHTML = '<p>Geen order ID gevonden in de URL.</p><a href="index.html">Terug naar restaurants</a>';
        }
        return;
    }

    try {
        const orderData = await getOrderTracking(orderId);

        // Render producten
        const productsList = document.getElementById('products-list');
        if (productsList) {
            productsList.innerHTML = orderData.shoppingCart.map(item => {
                const subtotal = item.price * item.quantity;
                return `<li>${item.name} - ${item.quantity}x ${formatPrice(item.price)} = ${formatPrice(subtotal)}</li>`;
            }).join('');
        }

        // Update totaalprijs
        const totalPrice = calculateTotal(orderData.shoppingCart);
        const totalPriceElement = document.getElementById('total-price');
        if (totalPriceElement) {
            totalPriceElement.textContent = formatPrice(totalPrice);
        }

        // Update order status
        const orderStatusElement = document.getElementById('order-status');
        if (orderStatusElement) {
            orderStatusElement.textContent = orderData.orderState || 'Onbekend';
        }

        // Toon content, verberg loading
        if (loadingElement) loadingElement.style.display = 'none';
        if (contentElement) contentElement.style.display = 'block';

    } catch (error) {
        console.error('Error loading order:', error);
        if (loadingElement) loadingElement.style.display = 'none';
        if (errorElement) errorElement.style.display = 'block';
    }
}

loadOrderTracking();
