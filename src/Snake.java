
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Snake extends JFrame {

    public Snake() {
        
        initUI();
    }
    private void initUI() {

        JPanel panel = new JPanel();
        getContentPane().add(panel);
                
    
        JButton singlePlayerButton = new JButton("Single Player");
        singlePlayerButton.addActionListener(e -> {
           add(new single_snake());
           panel.setSize(400,500);
           getContentPane().remove(panel);
           pack();   
        });
        panel.add(singlePlayerButton);
        
        JButton MultiPlayerButton = new JButton("1v1 Player");
        MultiPlayerButton.addActionListener(e -> {
           add(new Board());
           panel.setSize(400,500);
           getContentPane().remove(panel);
           pack();   

        });
        panel.add(MultiPlayerButton);

        JButton three_players = new JButton("3  Players");
        three_players.addActionListener(e -> {
           add(new three_player());
           panel.setSize(400,500);
           getContentPane().remove(panel);
           pack();   

        });
        panel.add(three_players);

        setTitle("Snake");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}
