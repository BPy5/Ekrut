package gui_client;

import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This class loads and shows the data of the requested order report
 */
public class ReportsCEOController implements Initializable{
	
	@FXML
	private ImageView image;
	@FXML
	private Button backBtn;
	
	@FXML
	private PieChart pieChart;
	
	ObservableList<PieChart.Data> pieChartData;
	MonthlyReportsController mpc;
	
	/**
	 * This method initializes and adds data to pie chart
	 * @param location - pointer to a "resources"
	 * @param resources - a locale-specific resource
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		String s = mpc.requestedReport;	//static variable

		ArrayList<PieChart.Data> data = new ArrayList<>();
		String[] d = s.split("\\.");
		String[] splitFinal = null;
			
		for (int i = 0; i < d.length; i++) {	// loop to split the data
			splitFinal = d[i].split(",");
			data.add(new PieChart.Data(splitFinal[0], Integer.parseInt(splitFinal[1])));	// add values to array list
		}
		pieChartData = FXCollections.observableArrayList(data);
		pieChart.getData().addAll(pieChartData);
	}
	
	
	/**
	 * This method hides the currently open window and shows monthlyReports.fxml
	 * @param event -  type of action
	 * @throws Exception - that a reasonable application might want to catch
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/MonthlyReports.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("Monthly Reports");
		primaryStage.setScene(scene);		
		primaryStage.show();	
	}




}
