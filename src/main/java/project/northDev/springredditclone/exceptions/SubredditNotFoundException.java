package project.northDev.springredditclone.exceptions;

public class SubredditNotFoundException extends RuntimeException{
        public SubredditNotFoundException(String message) {
            super(message);
        }

}
