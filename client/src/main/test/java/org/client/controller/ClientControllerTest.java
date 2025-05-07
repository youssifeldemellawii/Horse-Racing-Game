package org.client.controller;

import org.client.interfaces.ViewIF;
import org.client.view.MainView;
import org.client.view.GameView;
import org.client.view.LobbyView;
import org.client.view.StartView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

/**
 * Die Testklasse {@code ClientControllerTest} überprüft die Funktionalität des {@link ClientController}.
 * Es wird getestet, ob in den Methoden zur Einrichtung der verschiedenen Ansichten (Game, Lobby und Start)
 * die entsprechenden ActionListener korrekt an die Buttons gebunden werden.
 *
 * Dazu werden die Views mithilfe von Mockito gemockt und per Reflection in den Controller injiziert.
 */
class ClientControllerTest {

    private ClientController clientController;
    private MainView mainView;
    private GameView gameView;
    private LobbyView lobbyView;
    private StartView startView;

    /**
     * Initialisiert vor jedem Test die Testumgebung.
     * Es werden die Views gemockt, eine Spy-Instanz des ClientControllers erstellt und
     * das private Feld {@code mainView} per Reflection gesetzt.
     *
     * @throws Exception falls beim Zugriff auf das private Feld ein Fehler auftritt
     */
    @BeforeEach
    void setUp() throws Exception {
        // Mocking der Views
        mainView = mock(MainView.class);
        gameView = mock(GameView.class);
        lobbyView = mock(LobbyView.class);
        startView = mock(StartView.class);

        // ClientController mit Mock-View initialisieren (als Spy, um das Verhalten zu überwachen)
        clientController = Mockito.spy(ClientController.getInstance());

        // Reflection: Setzt das private Feld "mainView" im ClientController
        Field mainViewField = ClientController.class.getDeclaredField("mainView");
        mainViewField.setAccessible(true);
        mainViewField.set(clientController, mainView);

        // Stub: Rückgabe der gemockten Views bei Aufruf von getLobbyView(), getGameView() und getStartView()
        when(mainView.getLobbyView()).thenReturn(lobbyView);
        when(mainView.getGameView()).thenReturn(gameView);
        when(mainView.getStartView()).thenReturn(startView);
    }

    /**
     * Testet, ob beim Einrichten der GameView ein ActionListener dem RollDice-Button hinzugefügt wird.
     */
    @Test
    void testDiceButton() {
        JButton rollDiceButton = mock(JButton.class);
        when(gameView.getRollDiceButton()).thenReturn(rollDiceButton);

        clientController.setUpGameView();

        verify(rollDiceButton).addActionListener(any(ActionListener.class));
    }

    /**
     * Testet, ob beim Einrichten der LobbyView ein ActionListener für den Start-Button gesetzt wird.
     *
     * @throws Exception falls beim Zugriff auf das private Feld ein Fehler auftritt
     */
    @Test
    void testStartButtonListenerIsSet() throws Exception {
        // Erstelle einen Mock für den LobbyView (konkreter Typ, der in mainView verwendet wird)
        LobbyView lobbyViewMock = mock(LobbyView.class);

        // Erstelle einen Mock für MainView und stubbe die Methode getLobbyView()
        MainView mainViewMock = mock(MainView.class);
        when(mainViewMock.getLobbyView()).thenReturn(lobbyViewMock);

        // Injektion des gemockten MainView in den ClientController mittels Reflection
        Field mainViewField = clientController.getClass().getDeclaredField("mainView");
        mainViewField.setAccessible(true);
        mainViewField.set(clientController, mainViewMock);

        // Rufe die Methode auf, die den Listener setzen soll
        clientController.setUpLobbyView();

        // Verifiziere, dass setStartButtonListener mit einem ActionListener aufgerufen wurde
        verify(lobbyViewMock).setStartButtonListener(any(ActionListener.class));
    }

    /**
     * Testet, ob beim Einrichten der LobbyView ein ActionListener für den Ready-Button gesetzt wird.
     *
     * @throws Exception falls beim Zugriff auf das private Feld ein Fehler auftritt
     */
    @Test
    void testReadyButtonListenerIsSet() throws Exception {
        // Erstelle einen Mock für den LobbyView
        LobbyView lobbyViewMock = mock(LobbyView.class);

        // Optional: Erstelle einen Mock für den Ready-Button, falls getReadyButton() aufgerufen wird
        JButton readyButtonMock = mock(JButton.class);
        when(lobbyViewMock.getReadyButton()).thenReturn(readyButtonMock);

        // Erstelle einen Mock für MainView, der den gemockten LobbyView zurückgibt
        MainView mainViewMock = mock(MainView.class);
        when(mainViewMock.getLobbyView()).thenReturn(lobbyViewMock);

        // Injektion des gemockten MainView in den ClientController mittels Reflection
        Field mainViewField = clientController.getClass().getDeclaredField("mainView");
        mainViewField.setAccessible(true);
        mainViewField.set(clientController, mainViewMock);

        // Rufe die Methode auf, die den Listener setzen soll
        clientController.setUpLobbyView();

        // Verifiziere, dass setReadyButtonListener mit einem ActionListener aufgerufen wurde
        verify(lobbyViewMock).setReadyButtonListener(any(ActionListener.class));
    }

    /**
     * Testet, ob beim Einrichten der LobbyView ein ActionListener für den Exit-Button gesetzt wird.
     *
     * @throws Exception falls beim Zugriff auf das private Feld ein Fehler auftritt
     */
    @Test
    void testExitButton() throws Exception {
        // Erstelle einen Mock für den LobbyView (korrekter Typ laut MainView)
        LobbyView lobbyViewMock = mock(LobbyView.class);

        // Erstelle einen Mock für MainView
        MainView mainViewMock = mock(MainView.class);

        // Konfiguriere den MainView-Mock so, dass getLobbyView() den gemockten LobbyView zurückgibt
        when(mainViewMock.getLobbyView()).thenReturn(lobbyViewMock);

        // Injektion des gemockten MainView in den ClientController mittels Reflection
        Field mainViewField = clientController.getClass().getDeclaredField("mainView");
        mainViewField.setAccessible(true);
        mainViewField.set(clientController, mainViewMock);

        // Führe die Methode aus, die den Listener setzen soll
        clientController.setUpLobbyView();

        // Überprüfe, dass im LobbyView der Exit-Button-Listener registriert wurde
        verify(lobbyViewMock).setExitButtonListener(any(ActionListener.class));
    }

    /**
     * Testet, ob beim Einrichten der StartView ein ActionListener für den Join-Button gesetzt wird.
     *
     * @throws Exception falls beim Zugriff auf das private Feld ein Fehler auftritt
     */
    @Test
    void testJoinButtonListenerIsSet() throws Exception {
        // Erstelle einen Mock für StartView
        StartView startViewMock = mock(StartView.class);

        // Erstelle einen Mock für MainView und konfiguriere, dass getStartView() den gemockten StartView zurückgibt
        MainView mainViewMock = mock(MainView.class);
        when(mainViewMock.getStartView()).thenReturn(startViewMock);

        // Injektion des gemockten MainView in den ClientController mittels Reflection
        Field mainViewField = clientController.getClass().getDeclaredField("mainView");
        mainViewField.setAccessible(true);
        mainViewField.set(clientController, mainViewMock);

        // Rufe die Methode auf, die den Listener setzen soll
        clientController.setUpStartView();

        // Überprüfe, dass setJoinButtonListener mit einem ActionListener aufgerufen wurde
        verify(startViewMock).setJoinButtonListener(any(ActionListener.class));
    }

    /**
     * Testet, ob beim Einrichten der StartView ein ActionListener für den Create-Button gesetzt wird.
     *
     * @throws Exception falls beim Zugriff auf das private Feld ein Fehler auftritt
     */
    @Test
    void testCreateButtonListenerIsSet() throws Exception {
        // Erstelle einen Mock für StartView
        StartView startViewMock = mock(StartView.class);

        // Erstelle einen Mock für MainView und stubbe getStartView()
        MainView mainViewMock = mock(MainView.class);
        when(mainViewMock.getStartView()).thenReturn(startViewMock);

        // Injektion des gemockten MainView in den ClientController mittels Reflection
        Field mainViewField = clientController.getClass().getDeclaredField("mainView");
        mainViewField.setAccessible(true);
        mainViewField.set(clientController, mainViewMock);

        // Rufe die Methode auf, die den Listener setzen soll
        clientController.setUpStartView();

        // Überprüfe, dass der Listener über setCreateButtonListener gesetzt wurde
        verify(startViewMock).setCreateButtonListener(any(ActionListener.class));
    }
}
