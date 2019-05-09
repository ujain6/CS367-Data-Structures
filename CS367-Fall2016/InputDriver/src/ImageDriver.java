import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

/**
 * Using ImageIO to display the image by creating an ImageIcon, placing that
 * icon an a JLabel, and placing that label on a JFrame.
 */
public class ImageDriver {
    private InputDriver input;

    /**
     * Creates a ImageDriver
     * @param input the InputDriver needed for validation
     */
    public ImageDriver(InputDriver input) {
        this.input = input;
    }

    /**
     * Opens the image file by merging the content in the list.
     * @param list the list with correct order of packets
     * @throws BrokenImageException when the header or body of the image is broken
     * @throws IllegalArgumentException when list is null
     */
    public void openImage(PacketLinkedList<SimplePacket> list)
        throws BrokenImageException {
    	if (list == null) {
    		throw new IllegalArgumentException(
    				"Unable to open image because the linkedlist buffer is null.");
    	}
    	System.out.println("Loading image from buffer...");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        PacketLinkedListIterator<SimplePacket> iter = list.iterator();
        while (iter.hasNext()) {
            SimplePacket pkt = iter.next();
            try {
                outStream.write(pkt.getData());
            } catch (IOException e) {
                System.out.println("Unable to open the image for unknown reason.");
            }
        }
        byte[] data = outStream.toByteArray();
        final ByteArrayInputStream inStream = new ByteArrayInputStream(data);

        if (!input.validHeader(list.get(0))) {
            throw new BrokenImageException();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame editorFrame = new JFrame("Secret");
                editorFrame
                    .setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                double width = screenSize.getWidth();
                double height = screenSize.getHeight();
                BufferedImage image = null;
                try {
                    image = ImageIO.read(inStream);
                    Image img = image.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
                    ImageIcon imageIcon = new ImageIcon(img);
                    JLabel jLabel = new JLabel();

                    jLabel.setIcon(imageIcon);
                    JScrollPane scrPane = new JScrollPane(jLabel);
                    editorFrame.getContentPane().add(scrPane,
                            BorderLayout.CENTER);

                    editorFrame.pack();
                    editorFrame.setLocationRelativeTo(null);
                    editorFrame.setVisible(true);
                } catch(IOException e) {
                    System.out.println("Unable to open the image from the stream buffer.");
                } catch (NullPointerException e) {
                    System.out.println("Unable to open the image because of broken buffer.");
                }
            }
        });

        if (!input.validFile(list)) {
            throw new BrokenImageException();
        }

    }

}

