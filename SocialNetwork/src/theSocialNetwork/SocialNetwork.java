package theSocialNetwork;

import java.util.*;

public class SocialNetwork {

	private Map<String, Set<String>> graph; // Store user relationships

	private Set<String> users; // Store registered users

	private String loggedInUser; // Track the logged-in user

	public SocialNetwork() {

		graph = new HashMap<>();

		users = new HashSet<>();

		loggedInUser = null;

		initializeUsers();

	}

	// Initialize with preexisting users and friendships

	private void initializeUsers() {

		String[] initialUsers = { "Alice Johnson", "Bob Smith", "Charlie Brown", "David Williams", "Eva Miller" };

		for (String user : initialUsers) {

			signUp(user);

		}

		addFriendship("Alice Johnson", "Bob Smith");

		addFriendship("Alice Johnson", "Charlie Brown");

		addFriendship("Bob Smith", "David Williams");

		addFriendship("Charlie Brown", "Eva Miller");

		addFriendship("David Williams", "Eva Miller");

	}

	public void makefrnds() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Name of the user:");
		String frnd = sc.nextLine();
		if (!users.contains(frnd)) {
			System.out.println("User does not Exist");
			return;
		}
		addFriendship(loggedInUser, frnd);
	}

	// Sign up a new user

	public void signUp(String username) {

		if (!users.contains(username)) {

			users.add(username);

			graph.put(username, new HashSet<>());

			System.out.println(username + " signed up successfully.");

		} else {

			System.out.println("Username " + username + " is already taken.");

		}

	}

	// Log in an existing user

	public boolean login(String username) {

		if (users.contains(username)) {

			loggedInUser = username;

			System.out.println(username + " logged in successfully.");

			return true;

		} else {

			System.out.println("Username " + username + " does not exist. Please sign up.");

			return false;

		}

	}

	// Add a friendship between two users

	private void addFriendship(String u1, String u2) {

		graph.get(u1).add(u2);

		graph.get(u2).add(u1);

	}

	// Display friends of the logged-in user

	public void viewFriends() {
		Set<String> friends = graph.get(loggedInUser);

		if (friends.isEmpty()) {
			System.out.println("You have no friends yet.");
		} else {
			System.out.println("Your friends:");

			// Print table header
			System.out.printf("%-10s%n", "Name");
			System.out.println("----------");

			// Print each friend's name in a table row
			for (String frnd : friends) {
				System.out.printf("%-10s%n", frnd);
			}
		}
	}

	// Display top users on the platform

	public void viewTopUsers() {

		List<String> topUsers = new ArrayList<>(graph.keySet());

		topUsers.sort((a, b) -> Integer.compare(graph.get(b).size(), graph.get(a).size()));

		System.out.println("Top users on the platform: " + topUsers);

	}

	// Display friend recommendations for the logged-in user

	public void viewRecommendations() {

		List<String> recommendations = getRecommendations(loggedInUser);

		if (recommendations.isEmpty()) {

			System.out.println("No recommendations available. Make some friends first!");

		} else {

			System.out.println("Friend recommendations for you: " + recommendations);

		}

	}

	// Private helper to get friend recommendations for a user

	private List<String> getRecommendations(String user) {

		Map<String, Integer> suggestions = new HashMap<>();

		Set<String> directFriends = graph.get(user);

		for (String friend : directFriends) {

			for (String fof : graph.get(friend)) {

				if (!directFriends.contains(fof) && !fof.equals(user)) {

					suggestions.put(fof, suggestions.getOrDefault(fof, 0) + 1);

				}

			}

		}

		List<String> recommendationList = new ArrayList<>(suggestions.keySet());

		recommendationList.sort((a, b) -> Integer.compare(suggestions.get(b), suggestions.get(a)));

		return recommendationList;

	}

	// Calculate the degree of separation between the logged-in user and another
	// user

	public int getDegreeOfFriendship(String otherUser) {

		if (loggedInUser == null) {

			System.out.println("You must be logged in to check degree of friendship.");

			return -1;

		}

		if (loggedInUser.equals(otherUser)) {

			return 0; // No degree of separation if they are the same user

		}

		Set<String> visited = new HashSet<>();

		Queue<String> queue = new LinkedList<>();

		Map<String, Integer> distance = new HashMap<>(); // To store the distance of each user

		// Start from the logged-in user

		visited.add(loggedInUser);

		queue.add(loggedInUser);

		distance.put(loggedInUser, 0); // Distance to itself is 0

		while (!queue.isEmpty()) {

			String currentUser = queue.poll();

			int currentDistance = distance.get(currentUser);

			// Explore each friend of the current user

			for (String friend : graph.get(currentUser)) {

				if (!visited.contains(friend)) {

					visited.add(friend);

					queue.add(friend);

					distance.put(friend, currentDistance + 1);

					// If we found the target user

					if (friend.equals(otherUser)) {

						return distance.get(friend); // Return the degree of separation

					}

				}

			}

		}

		return -1; // If no path found, return -1 (users are not connected)

	}

	public static void main(String[] args) {

		SocialNetwork network = new SocialNetwork();

		Scanner scanner = new Scanner(System.in);

		boolean running = true;

		while (running) {

			System.out.println("\n--- Social Network Main Menu ---");

			System.out.println("1. Sign Up");

			System.out.println("2. Log In");

			System.out.println("3. Exit");

			System.out.print("Choose an option: ");

			int choice = scanner.nextInt();

			scanner.nextLine(); // Consume newline

			switch (choice) {

			case 1:

				System.out.print("Enter your name to sign up: ");

				String username = scanner.nextLine();

				network.signUp(username);

				break;

			case 2:

				System.out.print("Enter your name to log in: ");

				String loginName = scanner.nextLine();

				if (network.login(loginName)) {

					boolean userMenu = true;

					while (userMenu) {

						System.out.println("\n--- User Menu ---");

						System.out.println("1. View Friends");
						System.out.println("2. Make Friends");

						System.out.println("3. View Top Users");

						System.out.println("4. View Friend Recommendations");

						System.out.println("5. Calculate Degree of Friendship");

						System.out.println("6. Log Out");

						System.out.print("Choose an option: ");

						int userChoice = scanner.nextInt();

						scanner.nextLine(); // Consume newline

						switch (userChoice) {

						case 1:

							network.viewFriends();

							break;
						case 2:
							network.makefrnds();
							break;

						case 3:

							network.viewTopUsers();

							break;

						case 4:

							network.viewRecommendations();

							break;

						case 5:

							// Calculate the degree of friendship between the logged-in user and another
							// user

							System.out.print("Enter the name of the user to check the degree of friendship with: ");

							String otherUser = scanner.nextLine();

							int degree = network.getDegreeOfFriendship(otherUser); // Now it checks with the logged-in
																					// user

							if (degree == -1) {

								System.out.println("These users are not connected.");

							} else {

								System.out.println("Degree of friendship with " + otherUser + ": " + degree);

							}

							break;

						case 6:

							userMenu = false;
							break;
						}
					}
				}
				break;
			case 3:
				running = false;
				break;
			}
		}
		scanner.close();
	}
}
