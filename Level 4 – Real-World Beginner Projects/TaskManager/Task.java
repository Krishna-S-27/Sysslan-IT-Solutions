package TaskManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private String taskId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private TaskPriority priority;
    private TaskStatus status;
    private String createdDate;

    public enum TaskPriority {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High"),
        CRITICAL("Critical");

        private String label;

        TaskPriority(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public enum TaskStatus {
        PENDING("Pending"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");

        private String label;

        TaskStatus(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public Task(String taskId, String title, String description, LocalDate dueDate,
                TaskPriority priority, TaskStatus status) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.createdDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public boolean isOverdue() {
        return dueDate.isBefore(LocalDate.now()) && status != TaskStatus.COMPLETED;
    }

    public String toString() {
        return "Task ID: " + taskId + "\nTitle: " + title + "\nDescription: " + description +
                "\nDue Date: " + dueDate + "\nPriority: " + priority.getLabel() +
                "\nStatus: " + status.getLabel() + "\nCreated: " + createdDate;
    }
}
