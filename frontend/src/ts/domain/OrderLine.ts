import type {DishDto, OrderinformationDto} from "../presenter.ts";


export function setupDishes() {
    const urlParams = new URLSearchParams(window.location.search);
    const restaurantId = urlParams.get("id");
    if (!restaurantId) {
        console.error("Restaurant ID niet gevonden in URL parameters");
        return;
    }
    const orderLineButtons = document.querySelectorAll<HTMLButtonElement>(".add-btn");
    orderLineButtons.forEach(ol => {


        ol.addEventListener("click", async () => {
            const dishId = ol.id;
            const orderId = document.getElementById("orderId") as HTMLInputElement
            const quantityInput = document.getElementById(`quantity-${dishId}`) as HTMLInputElement;
            const dish:DishDto = await getInfoOfDish(dishId)
            const orderLineDto: ShoppingCartItem = {
                dishId: dishId,
                price:dish.price,
                quantity: Number(quantityInput.value),
                name:dish.name,
                preparationTime: dish.preparationTime
            }
            const order:Order = await addNewOrderLine(orderId.value,restaurantId, orderLineDto);
            if(order.orderId)
                orderId.value = order.orderId

            addToWinkelMandje(order)
        });
    })
}

async function getInfoOfDish(dishId: string) {
    const response = await fetch(`http://localhost:9090/api/order/dish/${dishId}`, {
        method: "GET"
    });

    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const data:DishDto = await response.json();
    return data
}

function addToWinkelMandje(order: Order) {
    const shoppingCart = document.getElementById("shoppingCart") as HTMLElement
    const html = order.shoppingCart
        .map(item => `
        <div class="cart-item">
            <span class="cart-item-name">${item.name}</span>
            <span class="cart-item-price">€${item.price.toFixed(2)}</span>
             <span class="cart-item-quantity">${item.quantity}</span>
        </div>
    `)
        .join('');
    shoppingCart.innerHTML=html
}
export async function addNewOrderLine(
    orderId: string,
    restaurantId: string,
    orderLine: ShoppingCartItem
) {
    const clientId = sessionStorage.getItem("client_id");

    if (!clientId) {
        alert("Geen gebruiker gevonden. Log eerst in.");
        throw new Error("Geen gebruiker gevonden. Log eerst in.");
    }

    const response = await fetch(
        `http://localhost:9090/api/order/${orderId}/shoppingCart/${restaurantId}?clientId=${clientId}`,
        {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(orderLine),
        }
    );

    if (!response.ok) {
        alert(`HTTP error! Status: ${response.status}`);
        throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const data: Order = await response.json();
    return data;
}


export async function prepareCheckout(orderId: string, restaurantId: string) {

    const cartResponse = await fetch(`http://localhost:9090/api/order/${orderId}/shoppingCart/`, {
        method: "GET"
    });

    const shoppingCart = await cartResponse.json();

    // Maak checkoutRequest-object aan dat het restaurant verwacht
    const checkoutRequest = {
        orderId: orderId,
        restaurantId: restaurantId,
        items: shoppingCart.map((item: ShoppingCartItem) => ({
            dishId: item.dishId,
            name: item.name,
            preparationTime: item.preparationTime ?? 10,
            price: item.price
        }))
    };

    // Stuur naar restaurant-service
    const response = await fetch(`http://localhost:8080/api/restaurant/prepareCheckout`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(checkoutRequest)
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `HTTP error! Status: ${response.status}`);
    }

    return await response.json();
}


export async function checkout(orderId: string, orderInformationDto: OrderinformationDto) {
    const clientId = sessionStorage.getItem("client_id");

    if (!clientId) {
        throw new Error("Geen gebruiker gevonden. Log eerst in.");
    }

    const response = await fetch(`http://localhost:9090/api/order/${orderId}/placeOrder?clientId=${clientId}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(orderInformationDto)
    });

    if (!response.ok) {
        throw new Error(`Fout bij plaatsen van bestelling: ${response.status}`);
    }

    return await response.json();
}





export interface ShoppingCartItem {
    dishId: string;
    price: number;
    quantity: number;
    name:string;
    preparationTime: number;
}

export interface Order {
    orderId: string;
    clientId: string;
    restaurantId: string;
    orderState: string;
    shoppingCart: ShoppingCartItem[];
}
export interface CheckoutRequest {
    orderId: string | null;
    restaurantId: string;
    clientId: string;
    items: Array<{
        dishId: string;
        name: string;
        price: number;
        quantity: number;
        preparationTime: number;
    }>;
}