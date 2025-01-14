package duke.task;

/**
 * Represents a task and consists of its description and completeness status.
 *
 * @author  Elumalai Oviya Dharshini
 * @version 1.1
 */
public class Task {
    private String description;
    private boolean isDone;

    /**
     * Constructor for Task specifying description.
     *
     * @param description description of task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks Task as complete.
     */
    public void markComplete() {
        this.isDone = true;
    }

    /**
     * Marks Task as incomplete.
     * Note: this is true by default for all Task objects.
     */
    public void markIncomplete() {
        this.isDone = false;
    }

    /**
     * Returns the state of completeness of Task.
     *
     * @return "X" if Task is completed, " " otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "\u2713" : " ");
    }

    /**
     * Default toString method that returns the description of Task.
     * Includes completion status.
     *
     * @return formatted string of the description and completeness status
     * of Task.
     */
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + description;
    }

    /**
     * Parses contents of Task into a csv-like format delimited by '|'.
     *
     * @return formatted string of Task and its completion status.
     */
    public String writeToFile() {
        String s = (isDone) ? "D" : "N";
        return s + " | " + description;
    }
}
