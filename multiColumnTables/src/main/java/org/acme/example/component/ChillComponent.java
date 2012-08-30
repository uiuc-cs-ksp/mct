package org.acme.example.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.components.JAXBModelStatePersistence;
import gov.nasa.arc.mct.components.ModelStatePersistence;
import gov.nasa.arc.mct.components.PropertyDescriptor;
import gov.nasa.arc.mct.components.TextInitializer;
import gov.nasa.arc.mct.components.PropertyDescriptor.VisualControlDescriptor;

//this class is a faked mock-up of ChillComponent. 
public class ChillComponent extends AbstractComponent {
	//for now, the contents of this class are mostly copy-pasted from ExampleComponent
	
	private final AtomicReference<ExampleModelRole> model = new AtomicReference<ExampleModelRole>(new ExampleModelRole());

	@Override
	protected <T> T handleGetCapability(Class<T> capability) {
		if (ModelStatePersistence.class.isAssignableFrom(capability)) {
		    JAXBModelStatePersistence<ExampleModelRole> persistence = new JAXBModelStatePersistence<ExampleModelRole>() {

				@Override
				protected ExampleModelRole getStateToPersist() {
					return model.get();
				}

				@Override
				protected void setPersistentState(ExampleModelRole modelState) {
					model.set(modelState);
				}

				@Override
				protected Class<ExampleModelRole> getJAXBClass() {
					return ExampleModelRole.class;
				}
		        
			};
			
			return capability.cast(persistence);
		}
		
		return null;
	}
	
	public ExampleModelRole getModel() {
		return model.get();
	}

	//this version is a mock-up (as of fri 10aug12 1538)
	@Override
	public List<PropertyDescriptor> getFieldDescriptors() {
		//debug: trying to find what's wrong with this method
		List<PropertyDescriptor> fields = new ArrayList<PropertyDescriptor>();
		fields.add(new PropertyDescriptor("testFieldAlex", 
				new TextInitializer(getModel().getData().getDataDescription()), VisualControlDescriptor.Label));
		return fields;
		/*
		// Provide an ordered list of fields for MultiColumn Table view mock-up
		List<PropertyDescriptor> fields = new ArrayList<PropertyDescriptor>();

		// Describe the field "dataDescription" in the business class MyData. 
		// Here we specify an immutable field, whereby its initial value is specified using a convenience class TextInitializer.
		String labelText = "World Swimming Event";
		PropertyDescriptor swimmingEvent = new PropertyDescriptor(labelText, 
				new TextInitializer(getModel().getData().getDataDescription()), VisualControlDescriptor.Label);

		// Describe MyData's field "doubleData". 
		// We specify a mutable text field.  The control display's values are maintained in the business model
		// via the PropertyEditor object.  When a new value is to be set, the editor also validates the prospective value.
		PropertyDescriptor swimmingWorldRecord = new PropertyDescriptor("Men's World Record (Rome 2009 Phelps)", 
				new TextPropertyEditor(this),  VisualControlDescriptor.TextField);
		swimmingWorldRecord.setFieldMutable(true);

		// Describe MyData's field "genderSelection". Here is a mutabl combo box visual control.  The control's initial value, 
		// and its selection states are taken from the business model via the PropertyEditor.
		PropertyDescriptor gender = new PropertyDescriptor("Gender", new EnumerationPropertyEditor(this),  VisualControlDescriptor.ComboBox);
		gender.setFieldMutable(true);

		// Describe MyData's field "verified".  This is a mutable check box visual control. 
		PropertyDescriptor verified = new PropertyDescriptor("Verified", new BooleanPropertyEditor(this),  VisualControlDescriptor.CheckBox);
		verified.setFieldMutable(true);
		
		fields.add(swimmingEvent);
		fields.add(swimmingWorldRecord);
		fields.add(gender);
		fields.add(verified);

		return fields;*/
	}
}
