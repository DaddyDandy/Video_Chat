/**
 * Created by Денис on 30.07.2014.
 */
function handleMessage(message) {
    var chatContent = $(PrimeFaces.escapeClientId('messageListContent')),
        text = message.toString();

    chatContent.append(text + '<br/>');

    $('#messageList').animate(
        {
            scrollTop : $('#messageList').find('.messageListContent')
                .height()
        }, 100);
}