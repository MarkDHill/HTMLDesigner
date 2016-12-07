import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class HTMLWriter {

	
	// Just a note that everything works EXCEPT for the delete function. 
	//It will redisplay the main menu but shouldn't cause a problem otherwise
	
	private static Scanner sc = new Scanner(System.in);
	private static String dirString = "HTMLFiles";
	private static String fileString = "HTMLFiles/webpage.html";
	private Path dirPath;
	private Path filePath;
	private File htmlFile;

	public static void main(String[] args) {

		ArrayList<String> tagArray = new ArrayList<>();
		boolean isValid = false;

		System.out.println("Welcome to the HTML writer, enjoy your stay!");

		System.out.println("When finished type END to write your webpage");
		System.out.println("Main Menu \n");

		createFile();
		
		while (isValid == false) {
			System.out.println("1. View current raw page");
			System.out.println("2. Write Tags");
			System.out.println("3. Write raw page to HTML");
			System.out.println("4. Delete Tag or Text");
			System.out.println("5. Exit Program without saving\n");

			int menuChoice = Validator.getInt(sc, "Please Enter choice: \n", 0, 6);

			if (menuChoice == 1) {
				if (tagArray.size() == 0) {
					System.out.println("You have not added anything to your page yet! \n");
				}
				for (String s : tagArray) {
					System.out.println(s + "\n");
					continue;
				}
			}

			if (menuChoice == 2) {
				boolean menu2 = false;

				System.out.println("Type MENU to return to main menu");
				System.out.println("(Note that end tags are automatically created!)");
				while (menu2 == false) {
					String input = Validator.getString(sc, "Enter a tag to add: ");
					if (input.equalsIgnoreCase("menu")) {
						menu2 = true;
						continue;
					} else {
						String ArrayPiece = StartTag(input);
						tagArray.add(ArrayPiece);
					}
					if (input.equalsIgnoreCase("html")) {
						continue;
					}
					if (input.equalsIgnoreCase("body")) {
						getBody(tagArray);
						continue;
					}

					if (input.equalsIgnoreCase("head")){
						String getTitle = Validator.getyn(sc, "Would you like to add a title? Y/N ");
						if (getTitle.equalsIgnoreCase("y")) {
							String newTitle = Validator.getLine(sc, "Please enter your title ");
							tagArray.add("<title>");
							tagArray.add(newTitle);
							tagArray.add("</title>");
							tagArray.add("</head>");
							System.out.println("Your header and title have been added and closed");
							continue;
						}
					}
					String choice = Validator.getyn(sc, "would you like to add text to this tag field? Y/N ");
					if (choice.equalsIgnoreCase("y")) {
						String textInput = Validator.getLine(sc, "Enter your Text: ");
						tagArray.add(textInput);
						String findLastTag = tagArray.get(tagArray.size() - 2);
						String endTagPiece = EndTag(findLastTag);
						tagArray.add(endTagPiece);
						if (tagArray.get(tagArray.size() - 1).equalsIgnoreCase("</footer>")) {
							tagArray.add("</html>");
						}
					} else {

						String lastAdd = tagArray.get(tagArray.size() - 1);
						if (lastAdd.startsWith("<")) {
							String endTagPiece = EndTag(lastAdd);
							tagArray.add(endTagPiece);
							if (tagArray.get(tagArray.size() - 1).equalsIgnoreCase("</footer>")) {
								tagArray.add("</html>");
							}
						}
					}
				}
			}

			if (menuChoice == 3) {
			
				
				FileWriter writer = null;
				try {
					writer = new FileWriter("HTMLFiles/webpage.html");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				for(String str: tagArray) {
				  try {
					writer.write(str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				System.out.println("Finished!");
				continue;
				
				
			}
			if (menuChoice == 4) {
				continue;
			}
			if (menuChoice == 5) {
				System.out.println("Goodbye!");
				break;
			}

		}
	}

	private static void getBody(ArrayList<String> tagArray) {
		boolean isBodyValid = false;
		System.out.println("Type END to finish your body tag");
		while (isBodyValid == false) {
			String newTagText = Validator.getLine(sc, "Would you like to add TEXT, TAG or END? ");
			if (!newTagText.equalsIgnoreCase("text") && !newTagText.equalsIgnoreCase("tag") && !newTagText.equalsIgnoreCase("end")) {
				System.out.println("ERROR: Please enter either TEXT, TAG or END");
				continue;
			}
			if (newTagText.equalsIgnoreCase("tag")) {
				String newBodyTag = Validator.getLine(sc, "Please add your tag: ");
				String ArrayPiece = StartTag(newBodyTag);
				tagArray.add(ArrayPiece);
				String tagText = Validator.getLine(sc, "Please add your tag text: ");
				tagArray.add(tagText);
				String lastAdd = tagArray.get(tagArray.size() - 2);
				if (lastAdd.startsWith("<")) {
					String endTagPiece = EndTag(lastAdd);
					tagArray.add(endTagPiece);
				}
				continue;
			}
			if (newTagText.equalsIgnoreCase("text")) {
				String newBodyText = Validator.getLine(sc, "Please add your text: ");
				tagArray.add(newBodyText);
				continue;
			}

			if (newTagText.equalsIgnoreCase("end")) {
				System.out.println("Your body has been added and closed");
				tagArray.add("</body>");
			//	sc.nextLine();
				isBodyValid = true;
				continue;
			}
		}
	}

	public static void createFile() {
		Path littleDirPath;
		littleDirPath = Paths.get(dirString);
		if (Files.notExists(littleDirPath)) {
			try {
				Files.createDirectories(littleDirPath);
				System.out.println("created the directory");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("IO exception");
				e.printStackTrace();
			}
		} else {

		}

		Path littleFilePath;
		littleFilePath = Paths.get(fileString);
		if (Files.notExists(littleFilePath)) {
			try {
				Files.createFile(littleFilePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;

	}

	public static String StartTag(String input) {

		StringBuilder sb = new StringBuilder(input).insert(0, '<').append('>');
		return sb.toString();

	}

	public static String EndTag(String exitTag) {

		StringBuilder sb = new StringBuilder(exitTag).insert(1, '/');

		return sb.toString();
	}

	public void CloseFile() {

	}

}
