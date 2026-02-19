# Tomato

Tomato is a task tracking chatbot gui app that helps people manage their tasks. It is optimized for cli usage with a gui display. Given below are instructions on how to use it.

## Quick start
1. Ensure java `17` is installed.
2. Download the latest `.jar` file from [here](https://github.com/ZealousGinger/ip/releases).
3. Copy the file to an empty folder.
4. Open a terminal, go to the folder, and run `java -jar tomato.jar` to run the app.
1. `todo work on Tomato readme`: Add a todo task.
2. `deadline submit cs2103t ip /by 20/2/2026 1600`: Add a deadline task.
3. `event cs2103t weekly briefing /from 20/2/2026 1600 /to 20/2/2026 1700`: Add an event task.
4. `mark 1`: Marks task as done.
5. `find cs2103t`: Finds tasks that matches tasks description with given keyword.
6. `delete 2`: Deletes tasks.
7. `list`: Lists all tasks.
7. `bye`: Exits the app.
8. `java -jar tomato.jar`: relaunch the app
9. `list`
10. `update 2 /to 20/2/2026 1800`: Updates the tasks end datetime.
11. `unmark 1`: Unmarks task as not done.
12. `list`


### References
- all images from [unsplash](https://unsplash.com/) and [pixabay](https://pixabay.com), specific image reference links are commented before their respective usages.
- usage of AI to help assist and generate Command class template and gui components and its logic. As well as add, check, fix, and improve code quality, documentation, and standards. See [AI.md](AI.md) for usage details.
  - Claude (write a few methods/classes)
    - used for generating Command class template.
    - generating new gui components such as ErrorDialogBox and its logic.
  - gpt-5.3-codex (refactor and improve/add on existing codebase)
    - used for improving code quality and java coding standards according to course given requirements.
    - used to add on and improve existing Javadoc and comments documentation.