package TaskManager;

import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskManager {
    private ArrayList<Task> tasks;
    private String filename;
    private int taskCounter;

    public TaskManager(String filename) {
        this.tasks = new ArrayList<>();
        this.filename = filename;
        this.taskCounter = 0;
        loadTasks();
    }

    public void addTask(Task task) {
        if(task == null) {
            System.out.println("Task cannot be null!");
            return;
        }

        for(Task t : tasks) {
            if(t.getTaskId().equals(task.getTaskId())) {
                System.out.println("Task with this ID already exists!");
                return;
            }
        }

        tasks.add(task);
        saveTasks();
        System.out.println("Task added successfully!");
    }

    public void deleteTask(String taskId) {
        for(int i = 0; i < tasks.size(); i++) {
            if(tasks.get(i).getTaskId().equals(taskId)) {
                Task removed = tasks.remove(i);
                saveTasks();
                System.out.println("Task deleted: " + removed.getTitle());
                return;
            }
        }
        System.out.println("Task not found!");
    }

    public Task searchTaskById(String taskId) {
        for(Task t : tasks) {
            if(t.getTaskId().equals(taskId)) {
                return t;
            }
        }
        return null;
    }

    public ArrayList<Task> searchTaskByTitle(String title) {
        ArrayList<Task> results = new ArrayList<>();
        for(Task t : tasks) {
            if(t.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(t);
            }
        }
        return results;
    }

    public ArrayList<Task> searchTaskByStatus(Task.TaskStatus status) {
        ArrayList<Task> results = new ArrayList<>();
        for(Task t : tasks) {
            if(t.getStatus() == status) {
                results.add(t);
            }
        }
        return results;
    }

    public ArrayList<Task> searchTaskByPriority(Task.TaskPriority priority) {
        ArrayList<Task> results = new ArrayList<>();
        for(Task t : tasks) {
            if(t.getPriority() == priority) {
                results.add(t);
            }
        }
        return results;
    }

    public ArrayList<Task> searchTaskByDueDate(LocalDate dueDate) {
        ArrayList<Task> results = new ArrayList<>();
        for(Task t : tasks) {
            if(t.getDueDate().equals(dueDate)) {
                results.add(t);
            }
        }
        return results;
    }

    public ArrayList<Task> getOverdueTasks() {
        ArrayList<Task> overdue = new ArrayList<>();
        for(Task t : tasks) {
            if(t.isOverdue()) {
                overdue.add(t);
            }
        }
        return overdue;
    }

    public ArrayList<Task> getTasksDueToday() {
        return searchTaskByDueDate(LocalDate.now());
    }

    public ArrayList<Task> getTasksDueTomorrow() {
        return searchTaskByDueDate(LocalDate.now().plusDays(1));
    }

    public void updateTask(String taskId) {
        Task task = searchTaskById(taskId);
        if(task == null) {
            System.out.println("Task not found!");
            return;
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("Current task:");
        System.out.println(task);

        System.out.print("\nEnter new title (or press Enter to skip): ");
        String title = sc.nextLine();
        if(!title.isEmpty()) {
            task.setTitle(title);
        }

        System.out.print("Enter new description (or press Enter to skip): ");
        String description = sc.nextLine();
        if(!description.isEmpty()) {
            task.setDescription(description);
        }

        System.out.print("Enter new due date YYYY-MM-DD (or press Enter to skip): ");
        String dueDateStr = sc.nextLine();
        if(!dueDateStr.isEmpty()) {
            try {
                LocalDate dueDate = LocalDate.parse(dueDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                task.setDueDate(dueDate);
            } catch(Exception e) {
                System.out.println("Invalid date format!");
            }
        }

        System.out.println("Select new priority (1=Low, 2=Medium, 3=High, 4=Critical, or press Enter to skip): ");
        String priorityStr = sc.nextLine();
        if(!priorityStr.isEmpty()) {
            try {
                int priorityChoice = Integer.parseInt(priorityStr);
                Task.TaskPriority[] priorities = Task.TaskPriority.values();
                if(priorityChoice >= 1 && priorityChoice <= priorities.length) {
                    task.setPriority(priorities[priorityChoice - 1]);
                }
            } catch(NumberFormatException e) {
                System.out.println("Invalid priority!");
            }
        }

        System.out.println("Select new status (1=Pending, 2=In Progress, 3=Completed, 4=Cancelled, or press Enter to skip): ");
        String statusStr = sc.nextLine();
        if(!statusStr.isEmpty()) {
            try {
                int statusChoice = Integer.parseInt(statusStr);
                Task.TaskStatus[] statuses = Task.TaskStatus.values();
                if(statusChoice >= 1 && statusChoice <= statuses.length) {
                    task.setStatus(statuses[statusChoice - 1]);
                }
            } catch(NumberFormatException e) {
                System.out.println("Invalid status!");
            }
        }

        saveTasks();
        System.out.println("Task updated successfully!");
    }

    public void displayAllTasks() {
        if(tasks.isEmpty()) {
            System.out.println("No tasks found!");
            return;
        }

        System.out.println("\n========== All Tasks ==========");
        System.out.println(String.format("%-8s | %-25s | %-12s | %-15s | %-12s",
                "Task ID", "Title", "Priority", "Status", "Due Date"));
        System.out.println("-----------------------------------------------------------");

        for(Task t : tasks) {
            System.out.println(String.format("%-8s | %-25s | %-12s | %-15s | %-12s",
                    t.getTaskId(),
                    t.getTitle().length() > 25 ? t.getTitle().substring(0, 22) + "..." : t.getTitle(),
                    t.getPriority().getLabel(),
                    t.getStatus().getLabel(),
                    t.getDueDate()));
        }
        System.out.println("================================\n");
    }

    public void displayTaskDetails(Task task) {
        if(task == null) {
            System.out.println("Task not found!");
            return;
        }

        System.out.println("\n========== Task Details ==========");
        System.out.println(task);
        if(task.isOverdue()) {
            System.out.println("Status: OVERDUE!");
        }
        System.out.println("==================================\n");
    }

    public void getTaskStatistics() {
        if(tasks.isEmpty()) {
            System.out.println("No tasks available!");
            return;
        }

        int completed = 0;
        int pending = 0;
        int inProgress = 0;
        int cancelled = 0;
        int overdue = 0;

        for(Task t : tasks) {
            switch(t.getStatus()) {
                case COMPLETED:
                    completed++;
                    break;
                case PENDING:
                    pending++;
                    break;
                case IN_PROGRESS:
                    inProgress++;
                    break;
                case CANCELLED:
                    cancelled++;
                    break;
            }

            if(t.isOverdue()) {
                overdue++;
            }
        }

        System.out.println("\n========== Task Statistics ==========");
        System.out.println("Total Tasks: " + tasks.size());
        System.out.println("Completed: " + completed);
        System.out.println("In Progress: " + inProgress);
        System.out.println("Pending: " + pending);
        System.out.println("Cancelled: " + cancelled);
        System.out.println("Overdue: " + overdue);
        System.out.println("Completion Rate: " + String.format("%.1f%%", (completed * 100.0) / tasks.size()));
        System.out.println("=====================================\n");
    }

    public void getPriorityReport() {
        if(tasks.isEmpty()) {
            System.out.println("No tasks available!");
            return;
        }

        Map<Task.TaskPriority, Integer> priorityCount = new HashMap<>();

        for(Task t : tasks) {
            priorityCount.put(t.getPriority(),
                    priorityCount.getOrDefault(t.getPriority(), 0) + 1);
        }

        System.out.println("\n========== Priority Report ==========");
        for(Task.TaskPriority priority : Task.TaskPriority.values()) {
            int count = priorityCount.getOrDefault(priority, 0);
            System.out.println(priority.getLabel() + ": " + count + " tasks");
        }
        System.out.println("=====================================\n");
    }

    public void getUpcomingTasks() {
        ArrayList<Task> upcoming = new ArrayList<>();

        for(int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            for(Task t : tasks) {
                if(t.getDueDate().equals(date) && t.getStatus() != Task.TaskStatus.COMPLETED) {
                    upcoming.add(t);
                }
            }
        }

        if(upcoming.isEmpty()) {
            System.out.println("No upcoming tasks for next 7 days!");
            return;
        }

        System.out.println("\n========== Upcoming Tasks (Next 7 Days) ==========");
        for(Task t : upcoming) {
            System.out.println("Due: " + t.getDueDate() + " | " + t.getTitle() +
                    " | Priority: " + t.getPriority().getLabel());
        }
        System.out.println("===================================================\n");
    }

    private void saveTasks() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            for(Task t : tasks) {
                writer.write(t.getTaskId() + "|" + t.getTitle() + "|" + t.getDescription() + "|" +
                        t.getDueDate() + "|" + t.getPriority().getLabel() + "|" +
                        t.getStatus().getLabel() + "|" + t.getCreatedDate());
                writer.newLine();
            }

            writer.close();
        } catch(IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    private void loadTasks() {
        try {
            File file = new File(filename);
            if(!file.exists()) {
                System.out.println("New task file will be created: " + filename + "\n");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int count = 0;

            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if(parts.length == 7) {
                    String taskId = parts[0].trim();
                    String title = parts[1].trim();
                    String description = parts[2].trim();
                    LocalDate dueDate = LocalDate.parse(parts[3].trim(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    Task.TaskPriority priority = Task.TaskPriority.valueOf(
                            parts[4].trim().toUpperCase().replace(" ", "_"));
                    Task.TaskStatus status = Task.TaskStatus.valueOf(
                            parts[5].trim().toUpperCase().replace(" ", "_"));

                    Task task = new Task(taskId, title, description, dueDate, priority, status);
                    tasks.add(task);
                    count++;

                    int id = Integer.parseInt(taskId.substring(1));
                    if(id > taskCounter) {
                        taskCounter = id;
                    }
                }
            }

            reader.close();
            System.out.println("Loaded " + count + " tasks from " + filename + "\n");
        } catch(IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }

    public String generateTaskId() {
        taskCounter++;
        return "T" + String.format("%04d", taskCounter);
    }

    public int getTotalTasks() {
        return tasks.size();
    }

    public void displayMenu() {
        System.out.println("\n========== Daily Task Manager ==========");
        System.out.println("1. Add New Task");
        System.out.println("2. View All Tasks");
        System.out.println("3. Search Task by ID");
        System.out.println("4. Search Task by Title");
        System.out.println("5. View Tasks by Status");
        System.out.println("6. View Tasks by Priority");
        System.out.println("7. View Overdue Tasks");
        System.out.println("8. View Tasks Due Today");
        System.out.println("9. View Upcoming Tasks");
        System.out.println("10. Update Task");
        System.out.println("11. Delete Task");
        System.out.println("12. Task Statistics");
        System.out.println("13. Priority Report");
        System.out.println("14. Exit");
        System.out.print("Enter choice: ");
    }

    private int getValidChoice() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if(choice >= 1 && choice <= 14) {
                    return choice;
                } else {
                    System.out.print("Enter a number between 1 and 14: ");
                }
            } catch(NumberFormatException e) {
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Daily Task Manager\n");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter filename to use (default: tasks.txt): ");
        String filename = sc.nextLine();

        if(filename.isEmpty()) {
            filename = "tasks.txt";
        }

        if(!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        TaskManager manager = new TaskManager(filename);

        boolean running = true;
        while(running) {
            manager.displayMenu();
            int choice = manager.getValidChoice();

            switch(choice) {
                case 1:
                    System.out.print("Enter task title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter task description: ");
                    String description = sc.nextLine();
                    System.out.print("Enter due date (yyyy-MM-dd): ");
                    String dueDateStr = sc.nextLine();

                    try {
                        LocalDate dueDate = LocalDate.parse(dueDateStr,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        System.out.println("Select priority (1=Low, 2=Medium, 3=High, 4=Critical): ");
                        int priorityChoice = Integer.parseInt(sc.nextLine());
                        Task.TaskPriority[] priorities = Task.TaskPriority.values();
                        Task.TaskPriority priority = priorities[priorityChoice - 1];

                        String taskId = manager.generateTaskId();
                        Task task = new Task(taskId, title, description, dueDate,
                                priority, Task.TaskStatus.PENDING);
                        manager.addTask(task);
                    } catch(Exception e) {
                        System.out.println("Invalid input!");
                    }
                    break;

                case 2:
                    manager.displayAllTasks();
                    break;

                case 3:
                    System.out.print("Enter task ID to search: ");
                    String searchId = sc.nextLine();
                    Task foundTask = manager.searchTaskById(searchId);
                    manager.displayTaskDetails(foundTask);
                    break;

                case 4:
                    System.out.print("Enter task title to search: ");
                    String searchTitle = sc.nextLine();
                    ArrayList<Task> foundTasks = manager.searchTaskByTitle(searchTitle);

                    if(foundTasks.isEmpty()) {
                        System.out.println("No tasks found with that title!");
                    } else {
                        System.out.println("Found " + foundTasks.size() + " task(s):");
                        for(Task t : foundTasks) {
                            System.out.println("- " + t.getTaskId() + ": " + t.getTitle());
                        }
                    }
                    break;

                case 5:
                    System.out.println("Select status (1=Pending, 2=In Progress, 3=Completed, 4=Cancelled): ");
                    int statusChoice = Integer.parseInt(sc.nextLine());
                    Task.TaskStatus[] statuses = Task.TaskStatus.values();
                    ArrayList<Task> statusTasks = manager.searchTaskByStatus(statuses[statusChoice - 1]);

                    if(statusTasks.isEmpty()) {
                        System.out.println("No tasks with that status!");
                    } else {
                        System.out.println("\nTasks with status: " + statuses[statusChoice - 1].getLabel());
                        for(Task t : statusTasks) {
                            System.out.println("- " + t.getTaskId() + ": " + t.getTitle());
                        }
                    }
                    break;

                case 6:
                    System.out.println("Select priority (1=Low, 2=Medium, 3=High, 4=Critical): ");
                    int priorChoice = Integer.parseInt(sc.nextLine());
                    Task.TaskPriority[] priors = Task.TaskPriority.values();
                    ArrayList<Task> priorTasks = manager.searchTaskByPriority(priors[priorChoice - 1]);

                    if(priorTasks.isEmpty()) {
                        System.out.println("No tasks with that priority!");
                    } else {
                        System.out.println("\nTasks with priority: " + priors[priorChoice - 1].getLabel());
                        for(Task t : priorTasks) {
                            System.out.println("- " + t.getTaskId() + ": " + t.getTitle());
                        }
                    }
                    break;

                case 7:
                    ArrayList<Task> overdue = manager.getOverdueTasks();
                    if(overdue.isEmpty()) {
                        System.out.println("No overdue tasks!");
                    } else {
                        System.out.println("\nOverdue Tasks:");
                        for(Task t : overdue) {
                            System.out.println("- " + t.getTaskId() + ": " + t.getTitle() +
                                    " (Due: " + t.getDueDate() + ")");
                        }
                    }
                    break;

                case 8:
                    ArrayList<Task> todayTasks = manager.getTasksDueToday();
                    if(todayTasks.isEmpty()) {
                        System.out.println("No tasks due today!");
                    } else {
                        System.out.println("\nTasks Due Today:");
                        for(Task t : todayTasks) {
                            System.out.println("- " + t.getTaskId() + ": " + t.getTitle());
                        }
                    }
                    break;

                case 9:
                    manager.getUpcomingTasks();
                    break;

                case 10:
                    System.out.print("Enter task ID to update: ");
                    String updateId = sc.nextLine();
                    manager.updateTask(updateId);
                    break;

                case 11:
                    System.out.print("Enter task ID to delete: ");
                    String deleteId = sc.nextLine();
                    manager.deleteTask(deleteId);
                    break;

                case 12:
                    manager.getTaskStatistics();
                    break;

                case 13:
                    manager.getPriorityReport();
                    break;

                case 14:
                    System.out.println("Thank you for using Task Manager!");
                    running = false;
                    break;
            }
        }

        sc.close();
    }
}