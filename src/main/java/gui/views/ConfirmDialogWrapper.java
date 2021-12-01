package gui.views;

import org.claspina.confirmdialog.ConfirmDialog;

import com.vaadin.flow.component.Component;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ConfirmDialogWrapper extends ConfirmDialog {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 8931570651441420236L;
	
	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	public ConfirmDialogWrapper() {
		super();
	}

	public ConfirmDialogWrapper withButton(Component component) {
		if (immutable) {
            throw new IllegalStateException("The dialog cannot be enhanced with a button after it has been opened.");
        }
        if (component != null) {
            buttonLayout.add(component);
            buttonAdded = true;
        }
		return this;
	}
}
