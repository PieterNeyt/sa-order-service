import "./css/style.css"
import "./css/restaurant.css"
import {type OrderinformationDto, showDishes, showRestaurants} from "./ts/presenter.ts";
import {checkout, prepareCheckout} from "./ts/domain/OrderLine.ts";


showRestaurants()
showDishes()


const prepareCheckoutBtn = document.querySelector<HTMLButtonElement>(".prepareCheckout-btn");
if (prepareCheckoutBtn) {
    prepareCheckoutBtn.addEventListener("click", async () => {
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
const checkoutBtn = document.querySelector<HTMLButtonElement>(".checkoutBtn");
if (checkoutBtn) {
    checkoutBtn.addEventListener("click", async () => {
        const orderIdInput = document.getElementById("orderId") as HTMLInputElement;
        const restaurantIdInput = document.getElementById("restaurantId") as HTMLInputElement;

        // Inputwaarden ophalen
        const name = (document.getElementById("name") as HTMLInputElement).value;
        const email = (document.getElementById("email") as HTMLInputElement).value;
        const street = (document.getElementById("street") as HTMLInputElement).value;
        const postalcode = (document.getElementById("postalcode") as HTMLInputElement).value;
        const city = (document.getElementById("city") as HTMLInputElement).value;

        // OrderinformationDto vullen
        const orderinformationDto: OrderinformationDto = { name, email, street, postalcode, city };

        try {
            const result = await checkout(orderIdInput.value, restaurantIdInput.value, orderinformationDto);
            console.log("Checkout successful:", result);

            // hier die order aanmaken in deze service
        } catch (error: any) {
            console.error("Checkout failed:", error);
            alert(`Checkout mislukt: ${error.message || error}`);
        }
    });
}