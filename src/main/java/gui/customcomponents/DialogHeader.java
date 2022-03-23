package gui.customcomponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

public class DialogHeader extends HorizontalLayout {

    private Button closeButton = new Button();
    public HorizontalLayout headerLayout;

    public DialogHeader(String title) {
        this.closeButton.setIcon(new Icon(VaadinIcon.CLOSE));
        this.closeButton.addClassNames("button", "no-background");
        this.closeButton.addClickListener(event -> fireEvent(new CloseEvent(this)));

        this.headerLayout = new HorizontalLayout(new H3(title), closeButton);
        this.headerLayout.setSizeFull();
        this.headerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        this.headerLayout.addClassName("dialog-header");
    }

    public Registration addCloseListener(
            ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    // Events
    public static abstract class DialogHeaderEvent extends ComponentEvent<DialogHeader> {
        public DialogHeaderEvent(DialogHeader source, Object o) {
            super(source, false);
        }
    }

    public static class CloseEvent extends DialogHeaderEvent {
        CloseEvent(DialogHeader source) {
            super(source, null);
        }
    }
}
