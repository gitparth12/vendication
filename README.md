## Vendication

_A Vending Machine Revolution. Bop it, Twist it, Vend it_
    
### How to use our app:

1. Clone the repository from GitHub.
2. After opening the repo on an IDE, cd into the SOFT2412-A2 folder.
3. To run, please enter 'gradle run --console=plain' into the terminal.
4. Upon opening, you'll see a list of the items in the vending machine that you can buy, organised by category.
5. Read our list of commands which appear right after our list of items. Each command should be followed by the exact information that you need to run that command e.g. signup < userType > < name > < username > < password >. 
6. Note that if you ever need to see the list of commands again you can type 'help' into the terminal. Further, if you need to see how to run any of our functionalities, you can run 'help < command >' into the terminal, where < command > is one of the commands printed when you enter 'help' into the terminal.
7. Enjoy! 



### Freddy's Requirements

- We don't have to implement a 'cancel transaction' command since our text interface allows you to reach any command from any other command
- We don't have to implement logic for hiding user password or card details (thank you!)
- We have to represent our summaries in a nice table format, especially the displaying of items in the vending machine right at the beginning of the program.
- Restrict user creation and signup- there should only be one owner and defined sellers and cashiers.
