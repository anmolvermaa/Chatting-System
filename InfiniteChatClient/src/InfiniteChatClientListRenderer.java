
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ImageIcon;
import javax.swing.ListCellRenderer;

public class InfiniteChatClientListRenderer extends JLabel implements ListCellRenderer{
	
	/**
	 * Method InfiniteChatClientListRenderer
	 *
	 *
	 */
	public InfiniteChatClientListRenderer(){}
		
     final static ImageIcon iconImage = new ImageIcon("../images/iChat_icon_small.gif");

     // This is the only method defined by ListCellRenderer.
     // We just reconfigure the JLabel each time we're called.

     public Component getListCellRendererComponent(
       JList list,              // the list
       Object value,            // value to display
       int index,               // cell index
       boolean isSelected,      // is the cell selected
       boolean cellHasFocus)    // does the cell have focus
     {
         String s = value.toString();
         setText(s);
         if(index>0){
         	setIcon(iconImage);
         }
         if (isSelected) {
             setBackground(list.getSelectionBackground());
             setForeground(list.getSelectionForeground());
         } else {
             setBackground(list.getBackground());
             setForeground(list.getForeground());
         }
         setEnabled(list.isEnabled());
         setFont(list.getFont());
         setOpaque(true);
         return this;
     }
 }
