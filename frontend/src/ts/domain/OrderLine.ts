
export function setupDishes() {
    const orderLineButtons = document.querySelectorAll<HTMLButtonElement>(".add-btn");
    orderLineButtons.forEach(ol => {


        ol.addEventListener("click", async () => {
            const dishId = ol.id;
            const orderId = document.getElementById("orderId") as HTMLInputElement
            const quantityInput = document.getElementById(`quantity-${dishId}`) as HTMLInputElement;
            const orderLineDto: ShoppingCartItem = {
                dishId: dishId,
                price:0,
                quantity: Number(quantityInput.value)
            }
            const order:Order = await addNewOrderLine(orderId.value, orderLineDto);
            if(order.orderId)
                orderId.value = order.orderId



            addToWinkelMandje(order)
        });
    })
}

function addToWinkelMandje(order: Order) {
    const shoppingCart = document.getElementById("shoppingCart") as HTMLElement
    const html = order.shoppingCart
        .map(item => `
        <div class="cart-item">
            <span class="cart-item-name">${item.dishId}</span>
            <span class="cart-item-price">€${item.price.toFixed(2)}</span>
             <span class="cart-item-quantity">${item.quantity}</span>
        </div>
    `)
        .join('');
    shoppingCart.innerHTML=html
}

export async function addNewOrderLine(orderId:string,orderLine:ShoppingCartItem){
    const response = await fetch(`/api/order/${orderId}/shoppingCart/`, {
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
}

export interface Order {
    orderId: string;
    clientId: string;
    restaurantId: string;
    shoppingCart: ShoppingCartItem[];
}