# swing-autocomplete

How to use the AutoCompleteField?

1. You must have a TableModel that hold your object value.

see https://github.com/mirzaakhena/swing-basetablemodel for more information about how to declare BaseTableModel

2. Declare the AutoCompleteField

    private AutoCompleteField<Person> autoCompleteField;
    
3. instanciate the autocompleteField then set the searchable method.

    autoCompleteField = new AutoCompleteField<>();
    autoCompleteField.setSearchable(this, new PersonTableModel());
    
4. implement the Searchable interface in your class. 

    public class MainFrame extends JFrame implements Searchable<Person> {
    
	    @Override
		public List<Person> search(String value) {
			List<Person> persons = personDao.findByName(value);
			return persons;
		}
	
		@Override
		public String getStringValue(Person x) {
			return x.getName();
		}
	
		@Override
		public void selectEvent(Person x) {
			if (x != null) {
				label.setText(x.getName() + ":" + x.getId());
			} else {
				label.setText("");
			}
		}
   
    }

That's all you need to set up to have a AutoCompleteField
