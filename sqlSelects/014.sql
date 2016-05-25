select chat.users.name, chat.messages.date, chat.messages.text from (chat.users right join chat.messages on chat.messages.user_id = chat.users.id) where text like "%hello%";
