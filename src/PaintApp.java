
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
import javax.swing.Icon;
import java.awt.Component;

public class PaintApp {
    
    // Método principal que inicia la aplicación configurando el Look and Feel del sistema
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo establecer el Look and Feel del sistema");
        }
        new PaintApp();
    }

    // Enumeración que define las herramientas de dibujo disponibles
    enum Tool {
        PENCIL, RECTANGLE, OVAL, ERASER
    }

    // Paleta de colores predefinida con tonos modernos y profesionales
    private static final Color[] COLOR_PALETTE = {
        new Color(34, 34, 34), new Color(220, 53, 69), new Color(40, 123, 222), 
        new Color(40, 167, 69), new Color(255, 193, 7), new Color(253, 126, 20),
        new Color(232, 62, 140), new Color(23, 162, 184), new Color(108, 117, 125),
        new Color(111, 66, 193)
    };

    // Clase para crear iconos personalizados dibujados programáticamente
    class CustomIcon implements Icon {
        private String type;
        private int size;
        private Color color;
        
        public CustomIcon(String type, int size, Color color) {
            this.type = type;
            this.size = size;
            this.color = color;
        }
        
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            switch (type) {
                case "PENCIL":
                    // Dibujar lápiz mejorado
                    g2d.setColor(new Color(139, 69, 19)); // Marrón para el lápiz
                    g2d.fillRect(x + 8, y + 2, 6, size - 8);
                    g2d.setColor(new Color(255, 215, 0)); // Dorado para la banda
                    g2d.fillRect(x + 8, y + 4, 6, 3);
                    g2d.setColor(Color.BLACK); // Negro para la punta
                    int[] xPoints = {x + 8, x + 14, x + 11};
                    int[] yPoints = {y + size - 6, y + size - 6, y + size - 2};
                    g2d.fillPolygon(xPoints, yPoints, 3);
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.drawRect(x + 8, y + 2, 6, size - 8);
                    break;
                    
                case "RECTANGLE":
                    // Dibujar rectángulo con relleno
                    g2d.setColor(new Color(100, 149, 237));
                    g2d.fillRect(x + 4, y + 6, size - 8, size - 12);
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRect(x + 4, y + 6, size - 8, size - 12);
                    break;
                    
                case "OVAL":
                    // Dibujar círculo con relleno
                    g2d.setColor(new Color(255, 182, 193));
                    g2d.fillOval(x + 4, y + 4, size - 8, size - 8);
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(x + 4, y + 4, size - 8, size - 8);
                    break;
                    
                case "ERASER":
                    // Dibujar borrador mejorado
                    g2d.setColor(new Color(255, 192, 203)); // Rosa claro
                    g2d.fillRoundRect(x + 4, y + 4, size - 8, size - 8, 4, 4);
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRoundRect(x + 4, y + 4, size - 8, size - 8, 4, 4);
                    // Líneas de textura
                    g2d.drawLine(x + 6, y + 8, x + size - 6, y + 8);
                    g2d.drawLine(x + 6, y + size - 8, x + size - 6, y + size - 8);
                    break;
                    
                case "CLEAR":
                    // Dibujar bote de basura mejorado
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.setStroke(new BasicStroke(2));
                    // Cuerpo del bote
                    g2d.drawRect(x + 6, y + 8, size - 12, size - 10);
                    // Tapa
                    g2d.drawLine(x + 4, y + 8, x + size - 4, y + 8);
                    g2d.drawLine(x + 8, y + 6, x + size - 8, y + 6);
                    // Líneas verticales
                    g2d.drawLine(x + 9, y + 10, x + 9, y + size - 4);
                    g2d.drawLine(x + 12, y + 10, x + 12, y + size - 4);
                    g2d.drawLine(x + 15, y + 10, x + 15, y + size - 4);
                    break;
                    
                case "PALETTE":
                    // Dibujar paleta mejorada
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(x + 2, y + 2, size - 4, size - 4);
                    g2d.setColor(Color.BLACK);
                    g2d.drawOval(x + 2, y + 2, size - 4, size - 4);
                    // Colores en la paleta
                    g2d.setColor(Color.RED);
                    g2d.fillOval(x + 5, y + 5, 4, 4);
                    g2d.setColor(Color.BLUE);
                    g2d.fillOval(x + size - 9, y + 5, 4, 4);
                    g2d.setColor(Color.GREEN);
                    g2d.fillOval(x + 5, y + size - 9, 4, 4);
                    g2d.setColor(Color.YELLOW);
                    g2d.fillOval(x + size - 9, y + size - 9, 4, 4);
                    break;
                    
                case "BRUSH":
                    // Dibujar pincel mejorado
                    g2d.setColor(new Color(139, 69, 19)); // Mango marrón
                    g2d.fillRect(x + 8, y + 2, 4, size - 8);
                    g2d.setColor(new Color(192, 192, 192)); // Férula plateada
                    g2d.fillRect(x + 7, y + size - 8, 6, 3);
                    g2d.setColor(Color.BLACK); // Cerdas
                    for (int i = 0; i < 3; i++) {
                        g2d.drawLine(x + 8 + i, y + size - 5, x + 8 + i, y + size - 2);
                    }
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.drawRect(x + 8, y + 2, 4, size - 8);
                    break;
                    
                case "STROKE":
                    // Dibujar líneas de diferentes grosores
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawLine(x + 2, y + 6, x + size - 2, y + 6);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawLine(x + 2, y + 10, x + size - 2, y + 10);
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawLine(x + 2, y + 16, x + size - 2, y + 16);
                    break;
            }
            
            g2d.dispose();
        }
        
        @Override
        public int getIconWidth() {
            return size;
        }
        
        @Override
        public int getIconHeight() {
            return size;
        }
    }

    // Clase que encapsula una forma con su color, grosor de línea y color de relleno
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

    // Panel principal donde se realizan todos los dibujos
    class DrawingPanel extends JPanel {
        private List<StrokedShape> shapes = new ArrayList<>();
        private StrokedShape currentShape;
        private Point startPoint;
        private Tool currentTool = Tool.PENCIL;
        private Color currentStrokeColor = new Color(34, 34, 34);
        private Color currentFillColor = new Color(255, 255, 255);
        private int strokeSize = 2;

        // Constructor que inicializa el panel de dibujo con configuración básica
        public DrawingPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createLoweredBevelBorder()));
            initializeMouseListeners();
        }

        // Configura los eventos del mouse para detectar clics y arrastres
        private void initializeMouseListeners() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        startPoint = e.getPoint();
                        currentShape = null;
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
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
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
                        handleMouseDrag(e.getPoint());
                    }
                }
            });
        }

        // Procesa el movimiento del mouse según la herramienta seleccionada
        private void handleMouseDrag(Point endPoint) {
            BasicStroke stroke = new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            
            switch (currentTool) {
                case PENCIL:
                    // Líneas con color y grosor seleccionado
                    shapes.add(new StrokedShape(
                        new Line2D.Double(startPoint, endPoint), currentStrokeColor, null, stroke, false));
                    startPoint = endPoint;
                    break;
                    
                case RECTANGLE:
                    // Rectángulos con línea y relleno, grosor seleccionado
                    currentShape = createRectangle(startPoint, endPoint, stroke);
                    break;
                    
                case OVAL:
                    // Círculos con línea y relleno, grosor seleccionado
                    currentShape = createOval(startPoint, endPoint, stroke);
                    break;
                    
                case ERASER:
                    // Borrador con grosor seleccionado
                    shapes.add(new StrokedShape(
                        new Line2D.Double(startPoint, endPoint), Color.WHITE, null, stroke, false));
                    startPoint = endPoint;
                    break;
            }
            repaint();
        }

        // Crea un rectángulo con colores de línea y relleno
        private StrokedShape createRectangle(Point start, Point end, BasicStroke stroke) {
            int x = Math.min(start.x, end.x);
            int y = Math.min(start.y, end.y);
            int width = Math.abs(end.x - start.x);
            int height = Math.abs(end.y - start.y);
            return new StrokedShape(new Rectangle2D.Double(x, y, width, height), 
                                  currentStrokeColor, currentFillColor, stroke, true);
        }

        // Crea un óvalo/círculo con colores de línea y relleno
        private StrokedShape createOval(Point start, Point end, BasicStroke stroke) {
            int x = Math.min(start.x, end.x);
            int y = Math.min(start.y, end.y);
            int width = Math.abs(end.x - start.x);
            int height = Math.abs(end.y - start.y);
            return new StrokedShape(new Ellipse2D.Double(x, y, width, height), 
                                  currentStrokeColor, currentFillColor, stroke, true);
        }

        // Herramienta Clear - elimina todo
        public void clearCanvas() {
            shapes.clear();
            currentShape = null;
            repaint();
        }

        // Setters y getters
        public void setCurrentTool(Tool tool) {
            this.currentTool = tool;
        }

        public void setCurrentStrokeColor(Color color) {
            this.currentStrokeColor = color;
        }

        public void setCurrentFillColor(Color color) {
            this.currentFillColor = color;
        }

        public Color getCurrentStrokeColor() {
            return this.currentStrokeColor;
        }

        public Color getCurrentFillColor() {
            return this.currentFillColor;
        }

        public void setStrokeSize(int size) {
            this.strokeSize = size;
        }

        // Renderizado con anti-aliasing
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Dibujar todas las formas
            for (StrokedShape strokedShape : shapes) {
                if (strokedShape.isFilled && strokedShape.fillColor != null) {
                    g2d.setColor(strokedShape.fillColor);
                    g2d.fill(strokedShape.shape);
                }
                
                g2d.setColor(strokedShape.strokeColor);
                g2d.setStroke(strokedShape.stroke);
                g2d.draw(strokedShape.shape);
            }
            
            // Dibujar forma actual en progreso
            if (currentShape != null) {
                if (currentShape.isFilled && currentShape.fillColor != null) {
                    g2d.setColor(currentShape.fillColor);
                    g2d.fill(currentShape.shape);
                }
                
                g2d.setColor(currentShape.strokeColor);
                g2d.setStroke(currentShape.stroke);
                g2d.draw(currentShape.shape);
            }
        }
    }

    // Variables para mostrar colores seleccionados
    private JPanel strokeColorPanel;
    private JPanel fillColorPanel;

    // Constructor principal
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

    private JFrame createMainFrame() {
        JFrame frame = new JFrame("Java Paint App - Session 4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        return frame;
    }

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
        
        JLabel titleLabel = new JLabel("Java Paint App - Session 4", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        return headerPanel;
    }

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
        gbc.gridy = 0;
        
        // Título de herramientas
        JLabel toolsLabel = new JLabel("Drawing Tools");
        toolsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        toolsLabel.setForeground(new Color(73, 80, 87));
        toolPanel.add(toolsLabel, gbc);
        
        gbc.gridy++;
        toolPanel.add(Box.createVerticalStrut(10), gbc);
        
        // Botones de herramientas con iconos
        addDrawingTools(toolPanel, drawingPanel, gbc);
        
        // Indicadores de colores seleccionados
        addColorIndicators(toolPanel, drawingPanel, gbc);
        
        // Paleta de colores
        addColorPalette(toolPanel, drawingPanel, gbc);
        
        // Botón Clear
        addClearButton(toolPanel, drawingPanel, gbc);
        
        return toolPanel;
    }

    private void addDrawingTools(JPanel toolPanel, DrawingPanel drawingPanel, GridBagConstraints gbc) {
        ButtonGroup toolGroup = new ButtonGroup();
        
        String[][] tools = {
            {"PENCIL", "Pencil"},
            {"RECTANGLE", "Rectangle"}, 
            {"OVAL", "Circle"},
            {"ERASER", "Eraser"}
        };
        
        for (int i = 0; i < tools.length; i++) {
            JToggleButton button = createToolButton(
                tools[i][0], tools[i][1], Tool.valueOf(tools[i][0]), 
                drawingPanel, i == 0
            );
            toolGroup.add(button);
            gbc.gridy++;
            toolPanel.add(button, gbc);
        }
    }

    private JToggleButton createToolButton(String iconType, String text, Tool tool, DrawingPanel drawingPanel, boolean selected) {
        JToggleButton button = new JToggleButton();
        button.setLayout(new BorderLayout());
        
        // Panel con icono
        JPanel iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        iconPanel.add(new JLabel(new CustomIcon(iconType, 24, Color.DARK_GRAY)));
        
        // Label con texto
        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        button.add(iconPanel, BorderLayout.CENTER);
        button.add(textLabel, BorderLayout.SOUTH);
        
        button.setPreferredSize(new Dimension(250, 70));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setSelected(selected);
        
        updateButtonStyle(button, selected);
        
        button.addActionListener(e -> {
            drawingPanel.setCurrentTool(tool);
            updateToolButtonStyles(button.getParent());
        });
        
        return button;
    }

    private void updateButtonStyle(JToggleButton button, boolean selected) {
        if (selected) {
            button.setBackground(new Color(67, 56, 202));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(73, 80, 87));
        }
    }

    private void updateToolButtonStyles(java.awt.Container parent) {
        for (java.awt.Component comp : parent.getComponents()) {
            if (comp instanceof JToggleButton) {
                JToggleButton btn = (JToggleButton) comp;
                updateButtonStyle(btn, btn.isSelected());
            }
        }
    }

    private void addColorIndicators(JPanel toolPanel, DrawingPanel drawingPanel, GridBagConstraints gbc) {
        gbc.gridy++;
        toolPanel.add(Box.createVerticalStrut(20), gbc);
        
        // Panel para mostrar colores seleccionados
        JPanel colorIndicatorPanel = new JPanel();
        colorIndicatorPanel.setLayout(new java.awt.GridLayout(2, 2, 10, 5));
        colorIndicatorPanel.setBackground(new Color(248, 249, 250));
        colorIndicatorPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Selected Colors",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 12)
        ));
        
        JLabel strokeLabel = new JLabel("Line:", SwingConstants.CENTER);
        strokeLabel.setFont(new Font("Arial", Font.BOLD, 11));
        
        JLabel fillLabel = new JLabel("Fill:", SwingConstants.CENTER);
        fillLabel.setFont(new Font("Arial", Font.BOLD, 11));
        
        strokeColorPanel = new JPanel();
        strokeColorPanel.setBackground(drawingPanel.getCurrentStrokeColor());
        strokeColorPanel.setPreferredSize(new Dimension(50, 40));
        strokeColorPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        fillColorPanel = new JPanel();
        fillColorPanel.setBackground(drawingPanel.getCurrentFillColor());
        fillColorPanel.setPreferredSize(new Dimension(50, 40));
        fillColorPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        colorIndicatorPanel.add(strokeLabel);
        colorIndicatorPanel.add(strokeColorPanel);
        colorIndicatorPanel.add(fillLabel);
        colorIndicatorPanel.add(fillColorPanel);
        
        gbc.gridy++;
        toolPanel.add(colorIndicatorPanel, gbc);
    }

    private void addColorPalette(JPanel toolPanel, DrawingPanel drawingPanel, GridBagConstraints gbc) {
        gbc.gridy++;
        toolPanel.add(Box.createVerticalStrut(10), gbc);
        
        JPanel paletteHeader = new JPanel(new BorderLayout());
        paletteHeader.setOpaque(false);
        paletteHeader.add(new JLabel(new CustomIcon("PALETTE", 20, Color.DARK_GRAY)), BorderLayout.WEST);
        
        JLabel colorLabel = new JLabel(" Color Palette");
        colorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        colorLabel.setForeground(new Color(73, 80, 87));
        paletteHeader.add(colorLabel, BorderLayout.CENTER);
        
        gbc.gridy++;
        toolPanel.add(paletteHeader, gbc);
        
        JLabel instructionLabel = new JLabel("<html><center>Left click: Line color<br>Right click: Fill color</center></html>");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        instructionLabel.setForeground(new Color(108, 117, 125));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy++;
        toolPanel.add(instructionLabel, gbc);
        
        gbc.gridy++;
        toolPanel.add(Box.createVerticalStrut(5), gbc);
        
        JPanel colorGrid = new JPanel(new java.awt.GridLayout(2, 5, 10, 10));
        colorGrid.setBackground(new Color(248, 249, 250));
        colorGrid.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        for (Color color : COLOR_PALETTE) {
            JPanel colorPanel = createColorButton(color, drawingPanel);
            colorGrid.add(colorPanel);
        }
        
        gbc.gridy++;
        toolPanel.add(colorGrid, gbc);
    }

    private JPanel createColorButton(Color color, DrawingPanel drawingPanel) {
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(color);
        colorPanel.setPreferredSize(new Dimension(35, 35));
        colorPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        colorPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        colorPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // Clic izquierdo: color de línea
                    drawingPanel.setCurrentStrokeColor(color);
                    strokeColorPanel.setBackground(color);
                    strokeColorPanel.repaint();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    // Clic derecho: color de relleno
                    drawingPanel.setCurrentFillColor(color);
                    fillColorPanel.setBackground(color);
                    fillColorPanel.repaint();
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                colorPanel.setBorder(BorderFactory.createLineBorder(new Color(67, 56, 202), 3));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                colorPanel.setBorder(BorderFactory.createRaisedBevelBorder());
            }
        });
        
        return colorPanel;
    }

    private void addClearButton(JPanel toolPanel, DrawingPanel drawingPanel, GridBagConstraints gbc) {
        gbc.gridy++;
        toolPanel.add(Box.createVerticalStrut(20), gbc);
        
        JButton clearBtn = new JButton();
        clearBtn.setLayout(new BorderLayout());
        
        JPanel iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        iconPanel.add(new JLabel(new CustomIcon("CLEAR", 20, Color.BLACK)));
        
        JLabel textLabel = new JLabel("Clear Canvas", SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 12));
        textLabel.setForeground(Color.BLACK);
        
        clearBtn.add(iconPanel, BorderLayout.WEST);
        clearBtn.add(textLabel, BorderLayout.CENTER);
        
        clearBtn.setPreferredSize(new Dimension(250, 50));
        clearBtn.setBackground(new Color(220, 53, 69));
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
        
        gbc.gridy++;
        toolPanel.add(clearBtn, gbc);
    }

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
        sizePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 30, 25));
        
        // Panel con icono para stroke size
        JPanel strokePanel = new JPanel();
        strokePanel.setOpaque(false);
        
        JPanel iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        iconPanel.add(new JLabel(new CustomIcon("STROKE", 24, Color.DARK_GRAY)));
        
        JLabel sizeLabel = new JLabel(" Stroke Size:");
        sizeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sizeLabel.setForeground(new Color(73, 80, 87));
        
        JSlider sizeSlider = new JSlider(1, 20, 2);
        sizeSlider.setMajorTickSpacing(5);
        sizeSlider.setMinorTickSpacing(1);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);
        sizeSlider.setOpaque(false);
        sizeSlider.setPreferredSize(new Dimension(400, 50));
        
        JLabel currentSizeLabel = new JLabel("2px");
        currentSizeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currentSizeLabel.setForeground(new Color(67, 56, 202));
        currentSizeLabel.setPreferredSize(new Dimension(50, 25));
        
        sizeSlider.addChangeListener(e -> {
            int size = sizeSlider.getValue();
            drawingPanel.setStrokeSize(size);
            currentSizeLabel.setText(size + "px");
        });
        
        strokePanel.add(iconPanel);
        strokePanel.add(sizeLabel);
        strokePanel.add(Box.createHorizontalStrut(10));
        strokePanel.add(sizeSlider);
        strokePanel.add(Box.createHorizontalStrut(10));
        strokePanel.add(currentSizeLabel);
        
        sizePanel.add(strokePanel);
        
        return sizePanel;
    }
}