package co.edu.uan.poo.lbs;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.DirectionsPane;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.service.directions.DirectionStatus;
import com.lynden.gmapsfx.service.directions.DirectionsRenderer;
import com.lynden.gmapsfx.service.directions.DirectionsRequest;
import com.lynden.gmapsfx.service.directions.DirectionsResult;
import com.lynden.gmapsfx.service.directions.DirectionsService;
import com.lynden.gmapsfx.service.directions.DirectionsServiceCallback;
import com.lynden.gmapsfx.service.directions.TravelModes;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;

import connection.ConsultaVista;
import connection.InsertTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LbsController implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {

	protected DirectionsService directionsService;
	protected DirectionsPane directionsPane;
	protected StringProperty from = new SimpleStringProperty();
	protected StringProperty to = new SimpleStringProperty();
	private int idGeocodigo;
	private int idGeocodigoInverso;
	private int idCalculoRuta;
	InsertTable insertTable;

	@FXML
	private TextField lonTextField;

	@FXML
	private Button idLimpiarRutas;

	@FXML
	private TextField latTextField;

	@FXML
	private TextField fromTextField;

	@FXML
	private TextField toTextField;

	@FXML
	private Button computeRouteButton;

	@FXML
	private Button locateButton;

	/**
	 * Objeto que permite la adición de un mapa de Google Maps en una aplicación
	 * JavaFX
	 */
	@FXML
	private GoogleMapView mapView;

	/**
	 * Objeto para especificar la dirección que se quiere buscar en el mapa
	 */
	@FXML
	private TextField addressTextField;

	/**
	 * Vista de mapa
	 */
	private GoogleMap map;

	/**
	 * Servicio de geocódigo para búsqueda de una direccións
	 */
	private GeocodingService geocodingService;
	private DecimalFormat formatter = new DecimalFormat("###.00000");
	private StringProperty address = new SimpleStringProperty();

	public void initialize(URL url, ResourceBundle rb) {
		to.bindBidirectional(toTextField.textProperty());
		from.bindBidirectional(fromTextField.textProperty());
		mapView.addMapInializedListener(this);
		address.bind(addressTextField.textProperty());
		mapView.addMapInializedListener(() -> ListenerMap());
		idGeocodigo = ConsultaVista.lastIdTable("Geocodigo");
		idGeocodigoInverso = ConsultaVista.lastIdTable("GeocodigoInverso");
		idCalculoRuta = ConsultaVista.lastIdTable("CalculoRuta");
		insertTable = new InsertTable();
	}

	@Override
	public void mapInitialized() {
		geocodingService = new GeocodingService();
		MapOptions mapOptions = new MapOptions();

		mapOptions.center(new LatLong(4.5792523, -74.1021777)).mapType(MapTypeIdEnum.ROADMAP).overviewMapControl(false)
				.panControl(false).rotateControl(false).scaleControl(false).streetViewControl(false).zoomControl(true)
				.zoom(12);

		map = mapView.createMap(mapOptions);
		directionsService = new DirectionsService();
		directionsPane = mapView.getDirec();
	}

	@FXML
	public void addressTextFieldAction(ActionEvent event) {
		geocodingService.geocode(address.get(), (GeocodingResult[] results, GeocoderStatus status) -> {

			LatLong latLong = null;

			if (status == GeocoderStatus.ZERO_RESULTS) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "No matching address found");
				alert.show();
				return;
			} else if (results.length > 1) {
				Alert alert = new Alert(Alert.AlertType.WARNING, "Multiple results found, showing the first one.");
				alert.show();
				latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(),
						results[0].getGeometry().getLocation().getLongitude());
			} else {
				latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(),
						results[0].getGeometry().getLocation().getLongitude());
			}
			lonTextField.clear();
			latTextField.clear();
			fromTextField.clear();
			toTextField.clear();
			map.clearMarkers(); // Se usa para eliminar todos los Markers del mapa.
			// Adicionar un market en la dirección encontrada
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(latLong).visible(Boolean.TRUE).title("Encontrado");
			Marker marker = new Marker(markerOptions);
			map.setCenter(latLong);
			map.addMarker(marker);
			idGeocodigo++;
			insertTable.agregarGeocodigo(idGeocodigo, addressTextField.getText());

		});
	}

	@FXML
	void computeRouteButtonAction(ActionEvent event) {
		map.clearMarkers();
		lonTextField.clear();
		latTextField.clear();
		addressTextField.clear();
		DirectionsRequest request = new DirectionsRequest(from.get(), to.get(), TravelModes.DRIVING);
		directionsService.getRoute(request, this, new DirectionsRenderer(true, mapView.getMap(), directionsPane));
		idCalculoRuta++;
		insertTable.agregarCalculoRuta(idCalculoRuta, from.get(), to.get());
	}

	@FXML
	void locateButtonAction(ActionEvent event) {
		addressTextField.clear();
		fromTextField.clear();
		toTextField.clear();
		LatLong latLong = new LatLong(Double.parseDouble(latTextField.getText()),
				Double.parseDouble(lonTextField.getText()));
		map.setCenter(latLong);

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(latLong).visible(Boolean.TRUE).title("Encontrado");
		Marker marker = new Marker(markerOptions);
		map.clearMarkers();
		map.addMarker(marker);

		idGeocodigoInverso++;
		insertTable.agregarGeocodigoInverso(idGeocodigoInverso, Float.parseFloat(lonTextField.getText()),
				Float.parseFloat(latTextField.getText()));
	}

	protected void ListenerMap() {
		map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
			LatLong latLong = event.getLatLong();
			latTextField.setText(formatter.format(latLong.getLatitude()));
			lonTextField.setText(formatter.format(latLong.getLongitude()));
		});
	}

	@FXML
	void idLimpiarRutasAction(ActionEvent event) {
		mapInitialized();
		lonTextField.clear();
		latTextField.clear();
		fromTextField.clear();
		toTextField.clear();
		addressTextField.clear();
	}

	@Override
	public void directionsReceived(DirectionsResult results, DirectionStatus status) {
	}
}
