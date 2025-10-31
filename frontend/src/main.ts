import "./css/style.css"
import "./css/restaurant.css"
import {type OrderinformationDto, showDishes, showRestaurants} from "./ts/presenter.ts";
import {prepareCheckout, preparePayment} from "./ts/domain/OrderLine.ts";
import {initNavbar} from "./ts/navbarLoader.ts";

initNavbar()
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
            alert(`Checkout preparation mislukt`);
        }
    });
}
const checkoutBtn = document.querySelector<HTMLButtonElement>("#checkoutBtn");
if (checkoutBtn) {
    checkoutBtn.addEventListener("click", async () => {
        const urlParams = new URLSearchParams(window.location.search);
        const orderId = urlParams.get("orderId");

        if (!orderId) {
            alert("Geen order ID gevonden.");
            return;
        }

        const name = (document.getElementById("name") as HTMLInputElement).value;
        const email = (document.getElementById("email") as HTMLInputElement).value;
        const street = (document.getElementById("street") as HTMLInputElement).value;
        const postalcode = (document.getElementById("postalcode") as HTMLInputElement).value;
        const city = (document.getElementById("city") as HTMLInputElement).value;

        const orderinformationDto: OrderinformationDto = { name, email, street, postalcode, city };

        try {
            const result = await preparePayment(orderId, orderinformationDto);
            console.log("Payment preparation successful:", result);

            // Redirect naar Mollie betaalpagina
            window.location.href = result.paymentUrl;

        } catch (error) {
            console.error("Payment preparation failed:", error);
            alert("Betaling voorbereiden mislukt");
        }
    });
}