/**
 * Created by Денис on 30.07.2014.
 */
function handleNotification(data) {
    switch (data.toString()) {
        case "update_ui":
            updatingUsersOnline();
            break;
        case "open_dialog":
            openDialog();
            break;
    }
}
