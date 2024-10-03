package com.example.bookstorepro;
import com.example.bookstorepro.Database.DB;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class BarChartExample extends Application {

    private static final DatePicker startDatePicker = new DatePicker();
    private static final DatePicker endDatePicker = new DatePicker();
    public static GridPane gridPane = new GridPane();
    private static final Label labelStartDate = new Label("Start Date:");
    private static final Label labelEndDate = new Label("End Date:");
    private static final Button okButton = new Button("OK");
    private static BarChart<String, Number> barChart;



    public static void main(String[] args) {
        launch(args);
    }

    public static GridPane buildChart() {

        gridPane.getChildren().removeIf(node -> node instanceof BarChart); // Remove any existing chart
        gridPane.getChildren().clear();
        gridPane.add(labelStartDate, 0, 1);
        gridPane.add(startDatePicker, 1, 1);
        gridPane.add(labelEndDate, 0, 2);
        gridPane.add(endDatePicker, 1, 2);
        gridPane.add(okButton, 2, 2);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        okButton.setOnAction(event -> {
            try {
                gridPane.getChildren().remove(barChart);
                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();


                if(startDate != null && endDate != null) {
                    // Connect to the database and retrieve data
                    Connection connection = DB.getConnection();
                    Statement statement = connection.createStatement();
                    String query = "SELECT * FROM transactions WHERE dateoftransaction BETWEEN '" + startDate + "' AND '" + endDate + "'";
                    ResultSet resultSet = statement.executeQuery(query);

                    // Create the bar chart
                    CategoryAxis xAxis = new CategoryAxis();
                    xAxis.setLabel("Librarians");
                    NumberAxis yAxis = new NumberAxis();
                    barChart = new BarChart<>(xAxis, yAxis);

                    // Add data to the chart
                    XYChart.Series<String, Number> series1 = new XYChart.Series<>();
                    XYChart.Series<String, Number> series2 = new XYChart.Series<>();

                    series1.setName("Books Sold");
                    series2.setName("Income");
                    int quantity, price;
                    quantity = 0;
                    price = 0;

                    while (resultSet.next()) {
                        String label = resultSet.getString("librarianName");
                        XYChart.Data<String, Number> data1 = new XYChart.Data<>(label, FindQuantity(resultSet.getString("librarianName")));
                        XYChart.Data<String, Number> data2 = new XYChart.Data<>(label, FindPrice(resultSet.getString("librarianName")));
                        Tooltip tooltip1 = new Tooltip(String.valueOf(FindQuantity(resultSet.getString("librarianName")+" books")));
                        Tooltip tooltip2 = new Tooltip("$" + FindPrice(resultSet.getString("librarianName")));
                        data1.setNode(tooltip1.getGraphic());
                        data2.setNode(tooltip2.getGraphic());
                        series1.getData().add(data1);
                        series2.getData().add(data2);

                    }




                    // Add the series to the chart
                    barChart.getData().addAll(series1, series2);

                    if (series1.getNode() != null) {
                        series1.getNode().setStyle("-fx-bar-fill: blue;");
                    }

                    if (series2.getNode() != null) {
                        series2.getNode().setStyle("-fx-bar-fill: green;");
                    }

                    // Add the chart to the grid pane
                    gridPane.add(barChart, 0, 4, 3, 1);

                    // Add a tooltip to each data point in the chart
                    for (XYChart.Series<String, Number> series : barChart.getData()) {
                        for (XYChart.Data<String, Number> data : series.getData()) {
                            Tooltip tooltip = createTooltip(data.getYValue().toString());
                            Tooltip.install(data.getNode(), tooltip);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        startDatePicker.setValue(null);
        endDatePicker.setValue(LocalDate.now());
        return gridPane;
    }

    private static Tooltip createTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setStyle("-fx-font-size: 15");
        return tooltip;
    }

    private static int FindQuantity(String librarian) throws SQLException {
        Connection connection = DB.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT Sum(quantity) FROM transactions WHERE librarianname='"+librarian+"' AND dateoftransaction between '"+startDatePicker.getValue()+"' and '"+endDatePicker.getValue()+"'";
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0; }

    private static double FindPrice(String librarian) throws SQLException {
        Connection connection = DB.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT Sum(price) FROM transactions WHERE librarianname='"+librarian+"' AND dateoftransaction between '"+startDatePicker.getValue()+"' and '"+endDatePicker.getValue()+"'";
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0; }


    @Override
    public void start(Stage primaryStage) throws Exception {
        buildChart();
        //primaryStage.setTitle("BookstorePro - Bar Chart Example");
       // Scene scene = new Scene(buildChart(), 800, 600);
       // primaryStage.setScene(scene);
       // primaryStage.show();

    }
}