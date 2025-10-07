import "./css/style.css"
import "./css/restaurant.css"
import {showDishes, showRestaurants} from "./ts/presenter.ts";
import {prepareCheckout} from "./ts/domain/OrderLine.ts";


showRestaurants()
showDishes()


const checkoutBtn = document.querySelector<HTMLButtonElement>(".checkout-btn");
if (checkoutBtn) {
    checkoutBtn.addEventListener("click", async () => {
        const orderIdInput = document.getElementById("orderId") as HTMLInputElement;
        const restaurantIdInput = document.getElementById("restaurantId") as HTMLInputElement;



        try {
           await prepareCheckout(orderIdInput.value,restaurantIdInput.value);

           console.log("Checkout preparation successful");
           window.location.href = `checkOutPage.html?id=${encodeURIComponent(orderIdInput.value)}`;

        } catch (error) {
            console.error("Checkout failed:", error);
            // @ts-ignore
            alert(`Checkout mislukt: ${error.message}`);
        }
    });
}