package com.example.bookstorepro;

/* VERY IMPORTANT!!!!!!
Copyright ©[2023] [John Nase, Sara Berberi]

This program code is the intellectual property of John Nase and Sara Berberi,
and is protected by copyright law. All rights reserved.

This program code may not be reproduced, distributed, or transmitted in any form or by any means,
including photocopying, recording, or other electronic or mechanical methods, without the prior
written permission of us, except in the case of brief quotations embodied in critical reviews
and certain other noncommercial uses permitted by copyright law. By using this program code,
you agree to abide by the terms of this copyright disclaimer. For permission requests or further
inquiries, please contact us.

Github: @sara-berberi @JohnNase

ALL RIGHTS RESERVED ®
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.example.bookstorepro.Database.DB;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Profit extends Application {

    private Label dateLabel;
    private Label countdownLabel;
    private Label revenueLabel;

    public GridPane createGUI() {

        // Create the date label and format it
        dateLabel = new Label();
        dateLabel.setText(LocalDate.now().toString());
        dateLabel.setStyle("-fx-font-size: 16; -fx-text-fill: white;");

        // Create the countdown label
        countdownLabel = new Label();
        countdownLabel.setStyle("-fx-font-size: 16; -fx-text-fill: white;");

        // Create the revenue label
        revenueLabel = new Label();
        revenueLabel.setText("" + LocalDate.now().getMonth().toString());
        revenueLabel.setStyle("-fx-font-size: 16; -fx-text-fill: white;");

        // Create the colored box to hold the date and time labels
        GridPane headerPane = new GridPane();
        headerPane.setAlignment(Pos.CENTER);
        headerPane.setPadding(new Insets(10));
        headerPane.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        headerPane.add(dateLabel, 0, 0);
        headerPane.add(countdownLabel, 1, 0);
        headerPane.add(revenueLabel, 2, 0);

        // Create the main grid pane and add the header
        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setPadding(new Insets(20));
        mainPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        mainPane.add(headerPane, 0, 0);

        // Set up the countdown animation
        LocalDateTime deadline = LocalDateTime.of(LocalDate.now().plusDays(1), LocalDateTime.MIN.toLocalTime());
        Timeline countdown = new Timeline(new KeyFrame(Duration.ZERO, e -> updateCountdown(deadline)), new KeyFrame(Duration.seconds(1)));
        countdown.setCycleCount(Animation.INDEFINITE);
        countdown.play();

        return mainPane;
    }

    private void updateCountdown(LocalDateTime deadline) {
        long secondsLeft = LocalDateTime.now().until(deadline, ChronoUnit.SECONDS);
        if (secondsLeft >= 0) {
            countdownLabel.setText(String.format("Time left: %02d:%02d:%02d", secondsLeft / 3600, (secondsLeft % 3600) / 60, secondsLeft % 60));
        } else {
            countdownLabel.setText("Time's up!");
        }
    }



    @Override
    public void start(Stage stage) throws Exception {
        GridPane gridPane;
        gridPane=createGUI();

        Scene scene = new Scene(gridPane,500,500);
        stage.setScene(scene);
        stage.show();
    }
}


/* VERY IMPORTANT!!!!!!
Copyright ©[2023] [John Nase, Sara Berberi]

This program code is the intellectual property of John Nase and Sara Berberi,
and is protected by copyright law. All rights reserved.

This program code may not be reproduced, distributed, or transmitted in any form or by any means,
including photocopying, recording, or other electronic or mechanical methods, without the prior
written permission of us, except in the case of brief quotations embodied in critical reviews
and certain other noncommercial uses permitted by copyright law. By using this program code,
you agree to abide by the terms of this copyright disclaimer. For permission requests or further
inquiries, please contact us.

Github: @sara-berberi @JohnNase

ALL RIGHTS RESERVED ®
 */