import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
//in Java is a part of the java.util package and provides a resizable array-like data structure.
 //Unlike arrays, ArrayList can dynamically grow and shrink as elements are added or removed. 
 //It's widely used due to its flexibility and simplicity.


class Task {
    private String description;
    private String time;
    private String date;

    public Task(String description, String time, String date) {
        this.description = description;
        this.time = time;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getDateTime() {
        return date + " " + time; // Combine date and time for comparison
    }
}

class ToDoListGUI {
    private JFrame frame; // reprsents the main window of app
    private DefaultListModel<String> taskListModel;// manages and stores the list of task in GUI
    private JList<String> taskList;// displays scrollabel list of tasks 
    private JTextField taskInputField;
    private JTextField timeInputField;
    private JTextField dateInputField;
    private ArrayList<Task> tasks; // serves backend D.s to store task

    public ToDoListGUI() {
        tasks = new ArrayList<>();
        createUI();
        startNotificationChecker();
    }

    private void createUI() {
        frame = new JFrame("To-Do List with Notifications");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));

        taskInputField = new JTextField();
        timeInputField = new JTextField(); 
        dateInputField = new JTextField(); 

        inputPanel.add(new JLabel("Task: "));
        inputPanel.add(taskInputField);
        inputPanel.add(new JLabel("Time (HH:mm): "));
        inputPanel.add(timeInputField);
        inputPanel.add(new JLabel("Date (yyyy-MM-dd): "));
        inputPanel.add(dateInputField);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);

        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(e -> addTask());

        JButton deleteTaskButton = new JButton("Delete Task");
        deleteTaskButton.addActionListener(e -> deleteTask());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addTaskButton);
        buttonPanel.add(deleteTaskButton);

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addTask() {
        String taskDescription = taskInputField.getText();
        String taskTime = timeInputField.getText();
        String taskDate = dateInputField.getText();// reads the user input  

        if (!taskDescription.isEmpty() && !taskTime.isEmpty() && !taskDate.isEmpty()) {
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime.parse(taskDate + " " + taskTime, dateTimeFormatter); // Validate format

                Task newTask = new Task(taskDescription, taskTime, taskDate);
                tasks.add(newTask);
                taskListModel.addElement(
                    taskDescription + " - " + taskDate + " at " + taskTime
                );
                taskInputField.setText("");
                timeInputField.setText("");
                dateInputField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date or time format. Use HH:mm for time and yyyy-MM-dd for date.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            tasks.remove(selectedIndex);
            taskListModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startNotificationChecker() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForNotifications();
            }
        }, 0, 60 * 1000); // Run every minute
    }

    private void checkForNotifications() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String currentDateTime = LocalDateTime.now().format(dateTimeFormatter);

        for (Task task : tasks) {
            if (task.getDateTime().equals(currentDateTime)) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame, "It's time for: " + task.getDescription(), "Task Reminder", JOptionPane.INFORMATION_MESSAGE);
                });
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListGUI::new);
    }
}