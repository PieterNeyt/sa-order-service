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

        const response = await fetch(`http://localhost:9090/api/order/restaurants/${restaurantId}`, {method: "GET"});
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

export interface RestaurantDto {
    id: string;
    name: string;
    dishes: DishDto[];
}

export interface DishDto {
    id: string;
    restaurantId:string;
    name: string;
    description: string;
    price: number;
    preparationTime:number;
}

export interface OrderinformationDto {
    name: string;
    email: string;
    street: string;
    postalcode: string;
    city: string;

}