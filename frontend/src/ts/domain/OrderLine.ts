import type {DishDto} from "../presenter.ts";


export function setupDishes() {
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
                name:dish.name
            }
            const order:Order = await addNewOrderLine(orderId.value,dish.RestaurantId, orderLineDto);
            if(order.orderId)
                orderId.value = order.orderId

            addToWinkelMandje(order)
        });
    })
}

async function getInfoOfDish(dishId: string) {
    const response = await fetch(`http://localhost:8080/api/restaurant/dish/${dishId}`, {
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

export async function addNewOrderLine(orderId:string,restaurantId:string,orderLine:ShoppingCartItem){
    const response = await fetch(`/api/order/${orderId}/shoppingCart/${restaurantId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(orderLine)
    });

    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const data:Order = await response.json();
    return data
}

export interface ShoppingCartItem {
    dishId: string;
    price: number;
    quantity: number;
    name:string;
}

export interface Order {
    orderId: string;
    clientId: string;
    restaurantId: string;
    shoppingCart: ShoppingCartItem[];
}