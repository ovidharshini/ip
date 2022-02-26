package duke;

import duke.task.*;

/**
 * The Duke program implements a simple task bot with CRUD functionality.
 * The program can add three different types of tasks (todo, deadline,
 * event), mark tasks as done, and delete tasks.
 *
 * @author  Elumalai Oviya Dharshini
 * @version 1.0
 */
public class Duke {
    private Storage storage;
    private TaskList tasks;

    /**
     * Marks Task as complete.
     *
     * @param input index of task
     * @return generic update message string
     * @throws Exception if an exception occurs in the saving of
     * data to the file at filePath or NumberFormatException
     */
    public String doHandler(String input) throws Exception {

        int i = Integer.parseInt(input.replaceAll("[^0-9]",
                "")) - 1;
        assert i <= tasks.size() && i >= 0 : "index of item should be within scope of tasklist";
      
        tasks.get(i).markComplete();
        assert tasks.get(i).getStatusIcon().equals("X") : "task should be marked done";

        storage.save(tasks);
        return Ui.showUpdateMessage();
    }

    /**
     * Marks Task as incomplete.
     *
     * @param input index of task
     * @return generic update message string
     * @throws Exception if an exception occurs in the saving of
     * data to the file at filePath or NumberFormatException
     */
    public String undoHandler(String input) throws Exception {
        int i = Integer.parseInt(input.replaceAll("[^0-9]",
                "")) - 1;
        System.out.println("i " + i + " tasks size:" + tasks.size());
        assert i <= tasks.size() && i >= 0 : "index of item should be within scope of tasklist";
      
        tasks.get(i).markIncomplete();
        assert tasks.get(i).getStatusIcon().equals(" ") : "task should be marked incomplete";

        storage.save(tasks);
        return Ui.showUpdateMessage();
    }

    /**
     * Deletes Task at a specified index from tasks.
     *
     * @param input index of task
     * @return string of list of tasks and deletion message.
     * @throws Exception if an exception occurs in the saving of
     * data to the file at filePath or NumberFormatException
     */
    public String deleteHandler(String input) throws Exception {
        int i = Integer.parseInt(input.replaceAll("[^0-9]",
                "")) - 1;
        assert i <= tasks.size() && i >= 0 : "index of item should be within scope of tasklist";
        tasks.remove(i);

        storage.save(tasks);
        return Ui.showDeleteMessage(tasks);
    }

    /**
     * Returns a string of the list of Tasks.
     *
     * @return formatted string of list of tasks.
     */
    public String listHandler() {
        return Ui.showListMessage(tasks);
    }

    /**
     * Adds a Todo to tasks and returns string about the addition.
     *
     * @param input description of Todo
     * @return string stating that Todo has been added.
     * @throws Exception if an exception occurs in the saving of
     * data to the file at filePath
     */
    public String todoHandler(String input) throws Exception {
        Todo t = new Todo(input);
        tasks.add(t);

        storage.save(tasks);
        return Ui.showTodoMessage() + t;
    }


    /**
     * Finds tasks with matching keywords in tasklist and returns them.
     *
     * @param input keyword to search for
     * @return string stating that matching tasks are found.
     */
    public String findHandler(String input) {
        return Ui.showFindMessage() + tasks.find(input).toString();
    }

    /**
     * Adds a Deadline task to tasks and returns string about the addition.
     *
     * @param input string containing description and dateTime of task
     * @return  string stating that a Deadline task has been added.
     * @throws Exception if an exception occurs in the saving of
     * data to the file at filePath
     */
    public String deadlineHandler(String input) throws Exception {
        String datetime = input.replaceAll(".* by ", "");
        input = input.replaceAll(" by .*", "");

        Deadline d = new Deadline(input, datetime);
        tasks.add(d);

        storage.save(tasks);
        return Ui.showDeadlineMessage() + d;
    }

    /**
     * Adds an Event task to tasks and returns string about the addition.
     *
     * @param input string containing description and dateTime of task
     * @return  string stating that an Event task has been added.
     * @throws Exception if an exception occurs in the saving of
     * data to the file at filePath
     */
    public String eventHandler(String input) throws Exception {
        String time = input.replaceAll(".* at ", "");
        input = input.replaceAll(" at .*", "");
        Event e = new Event(input, time);
        tasks.add(e);
        storage.save(tasks);
        return Ui.showEventMessage() + e;
    }

    /**
     * Adds a doAfter task to tasks and returns string about the addition.
     *
     * @param input string containing description and dateTime of task
     * @return  string stating that a DoAfter task has been added.
     * @throws Exception if an exception occurs in the saving of
     * data to the file at filePath
     */
    public String doAfterHandler(String input) throws Exception {
        String time = input.replaceAll(".* after ", "");
        input = input.replaceAll(" after .*", "");
        DoAfter e = new DoAfter(input, time);
        tasks.add(e);
        storage.save(tasks);
        return Ui.showDoAfterMessage() + e;
    }

    /**
     * Returns 'EXIT' string to signal program termination.
     *
     * @return goodbye signal
     */
    public String byeHandler() {
        return Ui.showByeMessage() + "EXIT";
    }

    /**
     * Returns response to indecipherable input.
     *
     * @param input string to respond to
     * @return generic strings indicative of incorrect input.
     */
    public String defaultHandler(String input) {
        return input.equals("")
                ? Ui.showEmptyMessage()
                : Ui.showDefaultMessage();
    }


    /**
     * Determines Duke's responses to user input.
     *
     * @param input user input
     * @return string of Duke's response.
     */
    public String getResponse(String input) {
        try {
            String command = Parser.parse(input, tasks);
            input = Parser.handleInput(input);

            switch (command) {
            case "list":
                return listHandler();
            case "do":
                return doHandler(input);
            case "undo":
                return undoHandler(input);
            case "delete":
                return deleteHandler(input);
            case "todo":
                return todoHandler(input);
            case "find":
                return findHandler(input);
            case "deadline":
                return deadlineHandler(input);
            case "event":
                return eventHandler(input);
            case "doafter":
                return doAfterHandler(input);
            case "bye":
                return byeHandler();
            default:
                return defaultHandler(input);
            }
        } catch (NumberFormatException e) {
            return Ui.showNumberFormatMessage();
        } catch (Exception e) {
            return Ui.showError(e.getMessage());
        }
    }

    /**
     * Constructor for Duke specifying UI and Storage.
     *
     * It loads Tasks from a file into tasks.
     * If there is an error with loading Tasks from the specified file, it
     * initializes tasks to bean empty TaskList.
     */
    public Duke(String filePath) {
        try {
            storage = new Storage(filePath);
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            System.out.println(Ui.showLoadingError());
            tasks = new TaskList();
        }
    }
}
