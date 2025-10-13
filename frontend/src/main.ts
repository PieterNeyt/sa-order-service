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
            window.location.href = `checkOutPage.html?orderId=${encodeURIComponent(orderIdInput.value)}&restaurantId=${encodeURIComponent(restaurantIdInput.value)}`;

        } catch (error) {
            console.error("Checkout preparation failed:", error);
            // @ts-ignore
            alert(`Checkout preparation mislukt: ${error.message}`);
        }
    });
}
const checkoutBtn = document.querySelector<HTMLButtonElement>("#checkoutBtn");
if (checkoutBtn) {
    checkoutBtn.addEventListener("click", async () => {
        const urlParams = new URLSearchParams(window.location.search);
        const orderId = urlParams.get("orderId");
        const restaurantId = urlParams.get("restaurantId");
        if (!orderId) {
            alert("Geen order ID gevonden.");
            return;
        }
        if (!restaurantId) {
            alert("Geen restaurantId gevonden.");
            return;
        }
        // Inputwaarden ophalen
        const name = (document.getElementById("name") as HTMLInputElement).value;
        const email = (document.getElementById("email") as HTMLInputElement).value;
        const street = (document.getElementById("street") as HTMLInputElement).value;
        const postalcode = (document.getElementById("postalcode") as HTMLInputElement).value;
        const city = (document.getElementById("city") as HTMLInputElement).value;

        // OrderinformationDto vullen
        const orderinformationDto: OrderinformationDto = { name, email, street, postalcode, city };

        try {
            const result = await checkout(orderId,restaurantId, orderinformationDto);
            console.log("Checkout successful:", result);
            window.location.href = `trackingPage.html?orderId=${encodeURIComponent(orderId)}`;

        } catch (error: any) {
            console.error("Checkout failed:", error);
            alert(`Checkout mislukt: ${error.message || error}`);
        }
    });
}
