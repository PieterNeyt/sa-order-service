import {setupDishes} from "./domain/OrderLine.ts";

export async function showRestaurants(){
    const restaurantHtml = document.getElementById("restaurant-list") as HTMLDivElement
    if(restaurantHtml) {
        const response = await fetch(`http://localhost:9090/api/order/restaurants`, {method: "GET"});
        const restaurants: Restaurant[] = await response.json();

        const html = restaurants
            .map(item => {
                const isOpen = item.isOpen;
                const statusClass = isOpen ? "open" : "closed";
                const statusText = isOpen ? "Open" : "Gesloten";

                // Als het restaurant gesloten is, gebruik een div in plaats van een a-tag
                if (!isOpen) {
                    return `
                    <div class="card closed-card" id="${item.id}">
                        <img src="${item.logo}" alt="${item.name} Logo" />
                        <div class="card-content">
                            <h2>${item.name}</h2>
                            <p class="price">${item.priceCategory}</p>
                            <p class="status ${statusClass}">${statusText}</p>
                        </div>
                    </div>
                    `;
                } else {
                    return `
                    <a href="restaurantPage.html?id=${item.id}" class="card" id="${item.id}">
                        <img src="${item.logo}" alt="${item.name} Logo" />
                        <div class="card-content">
                            <h2>${item.name}</h2>
                            <p class="price">${item.priceCategory}</p>
                            <p class="status ${statusClass}">${statusText}</p>
                        </div>
                    </a>
                    `;
                }
            }).join("");
        restaurantHtml.innerHTML = html;
    }
}

export async function showDishes(){
    const dishesHtml = document.getElementById("dishes-list") as HTMLDivElement

    if(dishesHtml) {
        const params = new URLSearchParams(window.location.search);
        const restaurantId = params.get('id');

        const response = await fetch(`http://localhost:9090/api/order/${restaurantId}/dishes`, {method: "GET"});
        const restaurant: RestaurantDto = await response.json();
        const html = restaurant.dishes
            .map(item => `
        <div class="menu-item">
            <img src="" alt="Sushi Set 1">
            <div class="menu-info">
                <h3>${item.name}</h3>
                <p>${item.description}</p>
                <p class="menu-price">€${item.price}</p>
                <p class="preparation-time">Dit item heeft een bereidingstijd van: ${item.preparationTime} minuten</p>
            </div>
            <label>
                <input type="number" class="quantity-input" id="quantity-${item.id}" min="1" value="1">
            </label>
            <button class="add-btn" id="${item.id}">Toevoegen</button>
        </div>
    `).join("")
        dishesHtml.innerHTML = html;
        setupDishes()

        const restaurantName = document.getElementById("restaurant-name") as HTMLElement
        restaurantName.innerHTML = restaurant.name

        const restaurantIdInput = document.getElementById("restaurantId") as HTMLInputElement;
        restaurantIdInput.value = restaurant.id;

    }
}

export interface Restaurant{
    id: string;
    name:string;
    restaurantType: string
    isOpen: boolean;
    priceCategory: string
    logo:string
}
// main interfaces
export interface RestaurantDto {
    id: string; // UUID
    ownerId: string; // UUID
    addressId: string; // UUID
    restaurantType: string; // BUFFET, etc.
    name: string;
    email: string;
    logo: string;
    dishes: DishDto[];
    isOpen: boolean;
    priceCategory: string; // NORMAL, etc.
    openingHours: OpeningHourDto[]; // leeg of gevuld
}

export interface DishDto {
    id: string; // UUID
    RestaurantId: string; // let op hoofdletter R, zoals in JSON
    name: string;
    description: string;
    price: number;
    dishState: string; // NOT_PUBLISHED, etc.
    restaurantId:string;
    preparationTime:number;
}


export interface OpeningHourDto {
    dayOfWeek: string; // MONDAY, TUESDAY, …
    openingTime: string; // HH:mm
    closingTime: string; // HH:mm
}

export interface OrderinformationDto {
    name: string;
    email: string;
    street: string;
    postalcode: string;
    city: string;

}