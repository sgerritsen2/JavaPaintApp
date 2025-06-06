// File: PaintApp.java
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GradientPaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.Box;

public class PaintApp {
    
    // Main method that starts the application by configuring the system Look and Feel
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Could not set system Look and Feel");
        }
        new PaintApp();
    }

    // Enumeration that defines available drawing tools
    enum Tool {
        PENCIL, RECTANGLE, OVAL, ERASER
    }

    // Predefined color palette with modern and professional tones
    private static final Color[] COLOR_PALETTE = {
        new Color(34, 34, 34), new Color(220, 53, 69), new Color(40, 123, 222), 
        new Color(40, 167, 69), new Color(255, 193, 7), new Color(253, 126, 20),
        new Color(232, 62, 140), new Color(23, 162, 184), new Color(108, 117, 125),
        new Color(111, 66, 193)
    };

    // Class that encapsulates a shape with its color, line thickness and fill color
    class StrokedShape {
        Shape shape;
        Color strokeColor;
        Color fillColor;
        BasicStroke stroke;
        boolean isFilled;
        
        public StrokedShape(Shape shape, Color strokeColor, Color fillColor, BasicStroke stroke, boolean isFilled) {
            this.shape = shape;
            this.strokeColor = strokeColor;
            this.fillColor = fillColor;
            this.stroke = stroke;
            this.isFilled = isFilled;
        }
    }

    // Main panel where all drawings are made
    class DrawingPanel extends JPanel {
        private List<StrokedShape> shapes = new ArrayList<>();
        private StrokedShape currentShape;
        private Point startPoint;
        private Tool currentTool = Tool.PENCIL;
        private Color currentStrokeColor = new Color(34, 34, 34);
        private Color currentFillColor = new Color(255, 255, 255); // White by default
        private int brushSize = 3;

        // Constructor that initializes the drawing panel with basic configuration
        public DrawingPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createLoweredBevelBorder()));
            initializeMouseListeners();
        }

        // Configures mouse events to detect clicks and drags
        private void initializeMouseListeners() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    // Only draw with left click (REQUIREMENT: Only Draw with left click)
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        startPoint = e.getPoint();
                        currentShape = null;
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // Only process left click for drawing (REQUIREMENT: Only Draw with left click)
                    if (e.getButton() == MouseEvent.BUTTON1 && currentShape != null) {
                        shapes.add(currentShape);
                        currentShape = null;
                        repaint();
                    }
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    // Only draw with left button pressed (REQUIREMENT: Only Draw with left click)
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
                        handleMouseDrag(e.getPoint());
                    }
                }
            });
        }

        // Processes mouse movement according to selected tool
        private void handleMouseDrag(Point endPoint) {
            BasicStroke stroke = new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            
            switch (currentTool) {
                case PENCIL:
                    shapes.add(new StrokedShape(
                        new Line2D.Double(startPoint, endPoint), currentStrokeColor, null, stroke, false));
                    startPoint = endPoint;
                    break;
                    
                case RECTANGLE:
                    currentShape = createRectangle(startPoint, endPoint, stroke);
                    break;
                    
                case OVAL:
                    currentShape = createOval(startPoint, endPoint, stroke);
                    break;
                    
                case ERASER:
                    BasicStroke eraserStroke = new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                    shapes.add(new StrokedShape(
                        new Line2D.Double(startPoint, endPoint), Color.WHITE, null, eraserStroke, false));
                    startPoint = endPoint;
                    break;
            }
            repaint();
        }

        // Creates a rectangle based on two points with specified thickness
        private StrokedShape createRectangle(Point start, Point end, BasicStroke stroke) {
            int x = Math.min(start.x, end.x);
            int y = Math.min(start.y, end.y);
            int width = Math.abs(end.x - start.x);
            int height = Math.abs(end.y - start.y);
            // REQUIREMENT: Fill the shape with the selected fill color
            return new StrokedShape(new Rectangle2D.Double(x, y, width, height), 
                                  currentStrokeColor, currentFillColor, stroke, true);
        }

        // Creates an oval based on two points with specified thickness
        private StrokedShape createOval(Point start, Point end, BasicStroke stroke) {
            int x = Math.min(start.x, end.x);
            int y = Math.min(start.y, end.y);
            int width = Math.abs(end.x - start.x);
            int height = Math.abs(end.y - start.y);
            // REQUIREMENT: Fill the shape with the selected fill color
            return new StrokedShape(new Ellipse2D.Double(x, y, width, height), 
                                  currentStrokeColor, currentFillColor, stroke, true);
        }

        // Removes all drawn shapes from canvas
        public void clearCanvas() {
            shapes.clear();
            currentShape = null;
            repaint();
        }

        // Sets current drawing tool
        public void setCurrentTool(Tool tool) {
            this.currentTool = tool;
        }

        // Sets current stroke color
        public void setCurrentStrokeColor(Color color) {
            this.currentStrokeColor = color;
        }

        // Sets current fill color
        public void setCurrentFillColor(Color color) {
            this.currentFillColor = color;
        }

        // Gets current stroke color
        public Color getCurrentStrokeColor() {
            return this.currentStrokeColor;
        }

        // Gets current fill color
        public Color getCurrentFillColor() {
            return this.currentFillColor;
        }

        // Sets brush/eraser size
        public void setBrushSize(int size) {
            this.brushSize = size;
        }

        // Renders all shapes on the panel with improved quality
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            
            for (StrokedShape strokedShape : shapes) {
                // Draw fill if shape requires it (REQUIREMENT: Fill the shape with the selected fill color)
                if (strokedShape.isFilled && strokedShape.fillColor != null) {
                    g2d.setColor(strokedShape.fillColor);
                    g2d.fill(strokedShape.shape);
                }
                
                // Draw the outline
                g2d.setColor(strokedShape.strokeColor);
                g2d.setStroke(strokedShape.stroke);
                g2d.draw(strokedShape.shape);
            }
            
            if (currentShape != null) {
                // Draw fill of current shape (REQUIREMENT: Fill the shape with the selected fill color)
                if (currentShape.isFilled && currentShape.fillColor != null) {
                    g2d.setColor(currentShape.fillColor);
                    g2d.fill(currentShape.shape);
                }
                
                // Draw outline of current shape
                g2d.setColor(currentShape.strokeColor);
                g2d.setStroke(currentShape.stroke);
                g2d.draw(currentShape.shape);
            }
        }
    }

    // Variables for color panels (REQUIREMENT: Add panels to reflect selected colors)
    private JPanel strokeColorPanel;
    private JPanel fillColorPanel;

    // Main constructor that assembles the entire application interface
    public PaintApp() {
        JFrame frame = createMainFrame();
        DrawingPanel drawingPanel = new DrawingPanel();
        
        frame.add(createHeaderPanel(), BorderLayout.NORTH);
        frame.add(drawingPanel, BorderLayout.CENTER);
        frame.add(createToolPanel(drawingPanel), BorderLayout.WEST);
        frame.add(createSizePanel(drawingPanel), BorderLayout.SOUTH);
        
        frame.setSize(1400, 900);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    // Creates and configures the main application window
    private JFrame createMainFrame() {
        JFrame frame = new JFrame("Java Paint App - Session Requirements Fulfilled");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    // Creates the top panel with title and gradient background
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(67, 56, 202),
                    0, getHeight(), new Color(79, 70, 229)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel titleLabel = new JLabel("🎨 Java Paint App - May 16th Session Requirements", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(0, 40));
        headerPanel.add(titleLabel);
        
        return headerPanel;
    }

    // Creates the left side panel with tools and color palette
    private JPanel createToolPanel(DrawingPanel drawingPanel) {
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new GridBagLayout());
        toolPanel.setBackground(new Color(248, 249, 250));
        toolPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)));
        toolPanel.setPreferredSize(new Dimension(280, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        
        JLabel toolsLabel = new JLabel("Drawing Tools");
        toolsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        toolsLabel.setForeground(new Color(73, 80, 87));
        gbc.gridy = 0;
        toolPanel.add(toolsLabel, gbc);
        
        gbc.gridy = 1;
        toolPanel.add(Box.createVerticalStrut(10), gbc);
        
        addDrawingTools(toolPanel, drawingPanel, gbc);
        addColorIndicators(toolPanel, drawingPanel, gbc);
        addColorPalette(toolPanel, drawingPanel, gbc);
        addClearButton(toolPanel, drawingPanel, gbc);
        
        return toolPanel;
    }

    // Adds drawing tool buttons to the side panel
    private void addDrawingTools(JPanel toolPanel, DrawingPanel drawingPanel, GridBagConstraints gbc) {
        ButtonGroup toolGroup = new ButtonGroup();
        
        String[][] tools = {
            {"✏️", "Pencil", "PENCIL"},
            {"⬜", "Rectangle", "RECTANGLE"}, 
            {"⭕", "Oval", "OVAL"},
            {"🧽", "Eraser", "ERASER"}
        };
        
        for (int i = 0; i < tools.length; i++) {
            JToggleButton button = createModernToolButton(
                tools[i][0], tools[i][1], Tool.valueOf(tools[i][2]), 
                drawingPanel, i == 0
            );
            toolGroup.add(button);
            gbc.gridy = i + 2;
            toolPanel.add(button, gbc);
        }
    }

    // Creates tool buttons with modern design and visual effects
    private JToggleButton createModernToolButton(String icon, String text, Tool tool, DrawingPanel drawingPanel, boolean selected) {
        JToggleButton button = new JToggleButton(
            "<html><div style='text-align: center'>" + 
            "<div style='font-size: 16px'>" + icon + "</div>" +
            "<div style='font-size: 10px; margin-top: 2px'>" + text + "</div></html>"
        );
        
        button.setPreferredSize(new Dimension(250, 60));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setSelected(selected);
        
        updateButtonStyle(button, selected);
        
        button.addActionListener(e -> {
            drawingPanel.setCurrentTool(tool);
            updateToolButtonStyles(button.getParent());
        });
        
        return button;
    }

    // Updates visual style of buttons according to their state
    private void updateButtonStyle(JToggleButton button, boolean selected) {
        if (selected) {
            button.setBackground(new Color(67, 56, 202));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
        }
    }

    // Updates style of all tool buttons
    private void updateToolButtonStyles(java.awt.Container parent) {
        for (java.awt.Component comp : parent.getComponents()) {
            if (comp instanceof JToggleButton) {
                JToggleButton btn = (JToggleButton) comp;
                updateButtonStyle(btn, btn.isSelected());
            }
        }
    }

    // REQUIREMENT: Add panels to reflect the selected colors
    private void addColorIndicators(JPanel toolPanel, DrawingPanel drawingPanel, GridBagConstraints gbc) {
        gbc.gridy = 7;
        toolPanel.add(Box.createVerticalStrut(20), gbc);
        
        // Panel to show selected colors (REQUIREMENT: Add panels to reflect selected colors)
        JPanel colorIndicatorPanel = new JPanel();
        colorIndicatorPanel.setLayout(new java.awt.GridLayout(2, 2, 10, 5));
        colorIndicatorPanel.setBackground(new Color(248, 249, 250));
        colorIndicatorPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(67, 56, 202), 2),
            "Selected Colors - May 16th Session",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(67, 56, 202)
        ));
        
        // Labels
        JLabel strokeLabel = new JLabel("Stroke Color:");
        strokeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        strokeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel fillLabel = new JLabel("Fill Color:");
        fillLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        fillLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Color panels with better size (REQUIREMENT: Add panel to reflect the selected color)
        strokeColorPanel = new JPanel();
        strokeColorPanel.setBackground(drawingPanel.getCurrentStrokeColor());
        strokeColorPanel.setPreferredSize(new Dimension(60, 50));
        strokeColorPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLoweredBevelBorder(),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        
        // REQUIREMENT: Add a panel to reflect the selected fill color
        fillColorPanel = new JPanel();
        fillColorPanel.setBackground(drawingPanel.getCurrentFillColor());
        fillColorPanel.setPreferredSize(new Dimension(60, 50));
        fillColorPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLoweredBevelBorder(),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        
        colorIndicatorPanel.add(strokeLabel);
        colorIndicatorPanel.add(strokeColorPanel);
        colorIndicatorPanel.add(fillLabel);
        colorIndicatorPanel.add(fillColorPanel);
        
        gbc.gridy = 8;
        toolPanel.add(colorIndicatorPanel, gbc);
    }

    // Adds selectable color palette to side panel
    private void addColorPalette(JPanel toolPanel, DrawingPanel drawingPanel, GridBagConstraints gbc) {
        gbc.gridy = 9;
        toolPanel.add(Box.createVerticalStrut(10), gbc);
        
        JLabel colorLabel = new JLabel("Color Palette");
        colorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        colorLabel.setForeground(new Color(73, 80, 87));
        gbc.gridy = 10;
        toolPanel.add(colorLabel, gbc);
        
        // REQUIREMENT: Select the color only when the click is the left click
        // REQUIREMENT: Select the fill color of the shape with the right click
        JLabel instructionLabel = new JLabel("<html><center><b>Session Instructions:</b><br>" +
            "Left click: Stroke Color<br>" +
            "Right click: Fill Color<br>" +
            "<i>Drawing only with left click</i></center></html>");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        instructionLabel.setForeground(new Color(108, 117, 125));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setBorder(BorderFactory.createLineBorder(new Color(40, 167, 69), 1));
        gbc.gridy = 11;
        toolPanel.add(instructionLabel, gbc);
        
        gbc.gridy = 12;
        toolPanel.add(Box.createVerticalStrut(5), gbc);
        
        JPanel colorGrid = new JPanel(new java.awt.GridLayout(2, 5, 8, 8));
        colorGrid.setBackground(new Color(248, 249, 250));
        colorGrid.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        for (Color color : COLOR_PALETTE) {
            JPanel colorPanel = createColorButton(color, drawingPanel);
            colorGrid.add(colorPanel);
        }
        
        gbc.gridy = 13;
        toolPanel.add(colorGrid, gbc);
    }

    // Creates individual color buttons with hover effects and support for both clicks
    private JPanel createColorButton(Color color, DrawingPanel drawingPanel) {
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(color);
        colorPanel.setPreferredSize(new Dimension(40, 40));
        colorPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createLoweredBevelBorder()));
        colorPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        colorPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // REQUIREMENT: Select the color only when the click is the left click
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // Left click: stroke color
                    drawingPanel.setCurrentStrokeColor(color);
                    strokeColorPanel.setBackground(color);
                    strokeColorPanel.repaint();
                }
                // REQUIREMENT: Select the fill color of the shape with the right click
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    // Right click: fill color
                    drawingPanel.setCurrentFillColor(color);
                    fillColorPanel.setBackground(color);
                    fillColorPanel.repaint();
                }
                
                colorPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLoweredBevelBorder(),
                    BorderFactory.createRaisedBevelBorder()));
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                colorPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createLoweredBevelBorder()));
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                colorPanel.setBorder(BorderFactory.createLineBorder(new Color(67, 56, 202), 3));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                colorPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createLoweredBevelBorder()));
            }
        });
        
        return colorPanel;
    }

    // Adds Clear button that removes all content from canvas
    private void addClearButton(JPanel toolPanel, DrawingPanel drawingPanel, GridBagConstraints gbc) {
        gbc.gridy = 15;
        toolPanel.add(Box.createVerticalStrut(20), gbc);
        
        JButton clearBtn = new JButton("🗑️ Clear Canvas");
        clearBtn.setPreferredSize(new Dimension(250, 50));
        clearBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clearBtn.setBackground(new Color(220, 53, 69));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFocusPainted(false);
        clearBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        clearBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        clearBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                clearBtn.setBackground(new Color(200, 35, 51));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                clearBtn.setBackground(new Color(220, 53, 69));
            }
        });
        
        clearBtn.addActionListener(e -> drawingPanel.clearCanvas());
        
        gbc.gridy = 16;
        toolPanel.add(clearBtn, gbc);
    }

    // Creates bottom panel with slider control for brush size
    private JPanel createSizePanel(DrawingPanel drawingPanel) {
        JPanel sizePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(248, 249, 250),
                    0, getHeight(), new Color(233, 236, 239)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sizePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        sizePanel.setPreferredSize(new Dimension(0, 100));
        
        JLabel sizeLabel = new JLabel("Brush Size:");
        sizeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sizeLabel.setForeground(new Color(73, 80, 87));
        
        JSlider sizeSlider = new JSlider(1, 30, 3);
        sizeSlider.setMajorTickSpacing(10);
        sizeSlider.setMinorTickSpacing(5);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);
        sizeSlider.setOpaque(false);
        sizeSlider.setPreferredSize(new Dimension(400, 60));
        
        JLabel currentSizeLabel = new JLabel("3px");
        currentSizeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        currentSizeLabel.setForeground(new Color(67, 56, 202));
        currentSizeLabel.setPreferredSize(new Dimension(50, 25));
        
        sizeSlider.addChangeListener(e -> {
            int size = sizeSlider.getValue();
            drawingPanel.setBrushSize(size);
            currentSizeLabel.setText(size + "px");
        });
        
        sizePanel.add(sizeLabel);
        sizePanel.add(Box.createHorizontalStrut(20));
        sizePanel.add(sizeSlider);
        sizePanel.add(Box.createHorizontalStrut(20));
        sizePanel.add(currentSizeLabel);
        
        return sizePanel;
    }
}