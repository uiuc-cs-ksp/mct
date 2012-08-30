package org.acme.example.view;

import java.util.EventListener;

/**
 * Defines a listener interface to notify when table labels have changed.
 */
public interface LabelChangeListener extends EventListener {

	/**
	 * Indicates that table labels have changed.
	 */
	void labelsChanged();

}
