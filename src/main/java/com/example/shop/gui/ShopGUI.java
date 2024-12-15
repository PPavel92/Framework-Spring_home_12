package com.example.shop.gui;

import com.example.shop.factory.ProductFactory;
import com.example.shop.model.Product;
import com.example.shop.service.CreditCardPayment;
import com.example.shop.service.PayPalPayment;
import com.example.shop.service.PaymentStrategy;
import com.example.shop.observer.Cart;
import com.example.shop.observer.CartItem;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ShopGUI extends Application {

    private Cart cart;
    private TextArea cartText;
    private Label totalLabel;
    private String customerName;
    private String customerSurname;
    private boolean isAuthenticated = false;
    private PaymentStrategy paymentStrategy;
    private VBox checkBoxesPanel; // Панель с чекбоксами для корзины

    public ShopGUI() {
        cart = new Cart();
    }

    @Override
    public void start(Stage primaryStage) {
        createMainShopWindow(primaryStage);
    }


    private void createMainShopWindow(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();
        VBox productListView = new VBox(10);
        productListView.setPadding(new javafx.geometry.Insets(20));


        String[] productNames = {
                "Ноутбук", "Смартфон", "Наушники", "Телевизор", "Планшет", "Монитор",
                "Клавиатура", "Мышь", "Камера", "Флешка"
        };

        double[] productPrices = {
                50000, 30000, 5000, 40000, 25000, 15000, 2000, 1500, 10000, 2000
        };


        for (int i = 0; i < productNames.length; i++) {
            Product product = ProductFactory.createProduct(productNames[i], productPrices[i]);
            Button productButton = new Button("Купить " + product.getDescription() + " - " + product.getPrice() + " руб.");
            productButton.setOnAction(e -> handleProductSelection(product));
            productListView.getChildren().add(productButton);
        }


        VBox cartPanel = new VBox(10);
        cartPanel.setPadding(new javafx.geometry.Insets(20));
        cartText = new TextArea("Ваша корзина:");
        cartPanel.getChildren().add(cartText);


        totalLabel = new Label("Общая сумма: 0 руб.");
        cartPanel.getChildren().add(totalLabel);


        checkBoxesPanel = new VBox(10);
        cartPanel.getChildren().add(checkBoxesPanel);


        Button removeButton = new Button("Удалить выбранные товары");
        removeButton.setOnAction(e -> handleRemoveItems());
        cartPanel.getChildren().add(removeButton);

        borderPane.setCenter(productListView);
        borderPane.setRight(cartPanel);


        HBox footer = new HBox(10);
        footer.setPadding(new javafx.geometry.Insets(20));
        Button checkoutButton = new Button("Перейти к оформлению");
        checkoutButton.setOnAction(e -> handleCheckout(primaryStage));
        footer.getChildren().add(checkoutButton);
        borderPane.setBottom(footer);


        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("Интернет-магазин");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleProductSelection(Product product) {
        cart.addItem(product.getDescription(), product.getPrice());
        updateCartDisplay();
    }


    private void updateCartDisplay() {
        cartText.setText("Ваша корзина:\n" + cart.getCartItemsString());
        totalLabel.setText("Общая сумма: " + cart.getTotalPrice() + " руб.");
        updateCheckBoxes();
    }


    private void updateCheckBoxes() {
        checkBoxesPanel.getChildren().clear();


        for (int i = 0; i < cart.getItems().size(); i++) {
            CartItem item = cart.getItems().get(i);
            CheckBox checkBox = new CheckBox("Удалить: " + item.getDescription() + " - " + item.getPrice() + " руб.");
            checkBox.setUserData(item);
            checkBoxesPanel.getChildren().add(checkBox);
        }
    }


    private void handleRemoveItems() {
        List<CheckBox> selectedCheckBoxes = new ArrayList<>();


        for (javafx.scene.Node node : checkBoxesPanel.getChildren()) {
            CheckBox checkBox = (CheckBox) node;
            if (checkBox.isSelected()) {
                selectedCheckBoxes.add(checkBox);
            }
        }


        for (CheckBox checkBox : selectedCheckBoxes) {
            CartItem item = (CartItem) checkBox.getUserData();
            cart.removeItem(item);
        }

        updateCartDisplay();
    }


    private void handleCheckout(Stage primaryStage) {
        if (!isAuthenticated) {
            showLoginDialog(primaryStage);
        } else {
            ChoiceDialog<String> paymentDialog = new ChoiceDialog<>("Кредитная карта", "Кредитная карта", "PayPal");
            paymentDialog.setTitle("Выбор способа оплаты");
            paymentDialog.setHeaderText("Выберите способ оплаты");
            paymentDialog.showAndWait().ifPresent(paymentMethod -> {
                if (paymentMethod.equals("Кредитная карта")) {
                    paymentStrategy = new CreditCardPayment();
                } else {
                    paymentStrategy = new PayPalPayment();
                }


                paymentStrategy.pay(cart.getTotalPrice());
                generateReceipt();
                showConfirmationDialog(primaryStage);
            });
        }
    }


    private void showLoginDialog(Stage primaryStage) {
        TextInputDialog loginDialog = new TextInputDialog();
        loginDialog.setTitle("Авторизация");
        loginDialog.setHeaderText("Введите ваше имя:");
        loginDialog.showAndWait().ifPresent(name -> {
            customerName = name;
            showSurnameDialog(primaryStage);
        });
    }

    private void showSurnameDialog(Stage primaryStage) {
        TextInputDialog surnameDialog = new TextInputDialog();
        surnameDialog.setTitle("Авторизация");
        surnameDialog.setHeaderText("Введите вашу фамилию:");
        surnameDialog.showAndWait().ifPresent(surname -> {
            customerSurname = surname;
            isAuthenticated = true;
            handleCheckout(primaryStage);
        });
    }


    private void generateReceipt() {
        String receipt = "Чек покупателя: \n" +
                "ФИО: " + customerName + " " + customerSurname + "\n" +
                "Дата: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\n" +
                "Товары:\n" + cart.getCartItemsString() +
                "Итого: " + cart.getTotalPrice() + " руб.";

        try (FileWriter writer = new FileWriter(new File("receipt.txt"), true)) {
            writer.write(receipt);
            writer.write("\n-------------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showConfirmationDialog(Stage primaryStage) {
        Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
        confirmationAlert.setTitle("Поздравляем с покупкой!");
        confirmationAlert.setHeaderText("Ваш заказ оформлен.");
        confirmationAlert.setContentText("Спасибо за покупку в нашем магазине.");
        confirmationAlert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
