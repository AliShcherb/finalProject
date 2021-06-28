const card = document.querySelector(".cardItem");
const cardContent = document.querySelector(".card-content");

card.addEventListener("click", () => {
    card.style.color = "magenta";
});
let user = {
    name: "Milk",
    quantity: 30,
    description: "White and fresh from local cows",
    distributor: "Ivan",
    price: 40
};

function addItemForm() {
    document.querySelector(".addItem").style.display = "block";
}

function closeItemForm() {
    document.querySelector(".addItem").style.display = "none";
}

function addGroupForm() {
    document.querySelector(".addGroup").style.display = "block";
}

function closeGroupForm() {
    document.querySelector(".addGroup").style.display = "none";
}
let items = [{
    name: "Milk",
    quantity: 30,
    description: "White and fresh from local cows",
    distributor: "Ivan",
    price: 40
}, {
    name: "Bread",
    quantity: 30,
    description: "Hot from oven",
    distributor: "Pavel",
    price: 40
}, {
    name: "Water",
    quantity: 30,
    description: "Refreshing",
    distributor: "Iren",
    price: 40
}, {
    name: "Milk",
    quantity: 30,
    description: "White and fresh from local cows",
    distributor: "Ivan",
    price: 40
}];

function addItem() {
    /*alert(document.getElementsByName("itName")[0].value + "\n" + document.getElementsByName("itQuantity")[0].value + "\n" + document.getElementsByName("itDescription")[0].value + "\n" + document.getElementsByName("itDistributor")[0].value + "\n" + document.getElementsByName("itPrice")[0].value);
     */
    let itemTemp = {
        name: document.getElementsByName("itName")[0].value,
        quantity: document.getElementsByName("itQuantity")[0].value,
        description: document.getElementsByName("itDescription")[0].value,
        distributor: document.getElementsByName("itDistributor")[0].value,
        price: document.getElementsByName("itPrice")[0].value
    };
    placeItem(itemTemp);
}

function addGroup() {
    /*alert(document.getElementsByName("itName")[0].value + "\n" + document.getElementsByName("itQuantity")[0].value + "\n" + document.getElementsByName("itDescription")[0].value + "\n" + document.getElementsByName("itDistributor")[0].value + "\n" + document.getElementsByName("itPrice")[0].value);
     */
    let groupTemp = {
        name: document.getElementsByName("gtName")[0].value,
        description: document.getElementsByName("grDescription")[0].value,
    };
    placeGroup(groupTemp);
}

function fillItems() {
    for (let index = 0; index < items.length; index++) {
        placeItem(items[index]);
    }
}

function placeItem(item) {
    const itemsContainer = document.querySelector(".itemPanel");
    let itemMarkup = '<div class="cardItem"><div class="card__content"><h3>Товар: ' + item.name + '</h3><p>Кількість: ' + item.quantity + '</p><p>Опис: ' + item.description + '</p><p>Виробник: ' + item.distributor + '</p><p>Ціна: ' + item.price + 'грн.</p></div></div>';
    itemsContainer.insertAdjacentHTML("afterbegin", itemMarkup);
}

function placeGroup(group) {
    const groupsContainer = document.querySelector(".groupPanel");
    let groupMarkup = '<div class="cardGroup"><h3>' + group.name + '</h3><p>' + group.description + '</p></div>';
    groupsContainer.insertAdjacentHTML("afterbegin", groupMarkup);
}