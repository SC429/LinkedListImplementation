/*  This class represents a Playlist of podcast episodes, where each
/*  episode is implemented as an object of type Episode. A user navigating
/*  a Playlist should be able to move between songs using Next or Previous.
/*
/*  To enable flexible navigation, the Playlist is implemented as
/*  a Circular Doubly Linked List where each episode has a link to both
/*  the next and the prev episodes in the list.
/*
/*  Additionally, the last Episode is linked to the head of the list (via next),
/*  and the head of the list is linked to that last Episode (via prev). That said,
/*  there is NO special "last" reference keeping track of the last Episode.
/*  But there is a "head" reference that should always refer to the first Episode.
 */
public class Playlist
{
	private Episode head;
	private int size;

	public Playlist()
	{
		head = null;
		size = 0;
	}

	public boolean isEmpty()
	{
		return head == null;
	}

	// Make sure that "size" is updated properly in other methods to
	// always reflect the correct number of episodes in the current playlist
	public int getSize()
	{
		return this.size;
	}

	// Displays the Episodes starting from the head and moving forward
	// Example code and its expected output:
	/*   Playlist pl = new Playlist();
   /*   pl.addLast("PlanetMoney",26.0);
   /*   pl.addLast("HowIBuiltThis",10);
   /*   pl.addLast("RadioLab",25.5);
   /*   pl.displayPlaylistForward();
   /* [BEGIN] (PlanetMoney|26.0MIN) -> (HowIBuiltThis|10.0MIN) -> (RadioLab|25.5MIN) [END]
	 */
	public void displayPlaylistForward()
	{
		String output = "[BEGIN] ";
		Episode current = head;

		while(current.next != head ){
			output += current + " -> ";
			current = current.next;
		}
		output += current + " [END]\n";
		System.out.println(output + "  ");
	}


	// Displays the Episodes starting from the end and moving backwards
	// Example code and its expected output:
	/*   Playlist pl = new Playlist();
   /*   pl.addLast("PlanetMoney",26.0);
   /*   pl.addLast("HowIBuiltThis",10);
   /*   pl.addLast("RadioLab",25.5);
   /*   pl.displayPlaylistForward();
   /* [END] (RadioLab|25.5MIN) -> (HowIBuiltThis|10.0MIN) -> (PlanetMoney|26.0MIN) [BEGIN]
	 */
	public void displayPlaylistBackward()
	{
		String output = "[Playing Backwards:]";
		Episode current = head.prev;


		if (size == 0) {
			System.out.println("The list is empty!"); // try case for displayplaylistbackwards

		} else {
			while(current != head) {  // add new Episodes into the playlist
				output += current + "->";
				current = current.prev;
			}
			output+=current + "[END]\n";
			System.out.println(output);
		}
	}

	//adds an episode to the beginning of the playlist
	public void addFirst(String title, double duration )
	{

		if (size == 0) {    // if the playlist is empty
			head = new Episode (title, duration, head, head);
			head.next = head;
			head.prev = head;
		} else {      //if playlist is not empty
			Episode last = head.prev;
			Episode oldHead = head;
			head = new Episode (title, duration, oldHead, last);
			oldHead.prev = head;
			last.next = head;
		}
		size++;


	}	


	// Add a new Episode at the end of the Playlist
	public void addLast( String title, double duration )
	{
		if (size == 0) {		//if the playlist is empty
			addFirst(title, duration);
		} else {		//if the playlist is not empty
			Episode last = head.prev;
			Episode first = head;
			Episode newLast = new Episode(title, duration, first, last);
			first.prev = newLast;
			last.next = newLast;
		}
		size++;   
	}

	//adds an episode at a certain index, pushing elements behind it to the right
	public void add(String title, double duration, int index )
	{

		Episode current = head;
		Episode previous = null;

		if (index > size) {		//if the index is bigger than the size of the playlist
			throw new RuntimeException("index is out of bounds! enter a number from 0 to" + size);
		}

		if (size == 0) {		// if the playlist is empty
			addFirst(title,duration);
		} else {		//if the playlist is not empty
			for (int i = 0; i < index; i++) { //iterate to specified position
				previous = current;			  // position != index
				current = current.next;
			}

			Episode newEpisode = new Episode(title, duration, current, previous);
			previous.next = newEpisode; //perform the insertion 
			current.prev = newEpisode;
			size++;
		}  

	}


	public Episode deleteFirst()
	{
		Episode current = head;

		if (size == 0) { //corner case
			throw new RuntimeException("cannot delete from an empty playlist!");
		}
		else if (size == 1){ //corner case
			current.next = null;
			head = current;
			size--;
			return head;
		}
		else {
			Episode previous = null;
			while(current.next != head) {
				previous = current;
				current = current.next;
			}
			current.next = head.next;
			head = head.next;
			head.prev = current;

			return current;
		}
	}

	//deletes last episode from playlist
	public Episode deleteLast() 
	{

		Episode current = head;
		if (size == 0) // empty playlist
			throw new RuntimeException("cannot delete from an empty playlist!");
		if (size == 1) {   // corner case - size of 1 playlist
			current.next = null;
			head = current;
			size--;
			return head;

		} else {   // size is greater than 1
			Episode oldLast = head.prev; //return condition
			Episode newLast = oldLast.prev;

			head.prev = new Episode (newLast.getTitle(),newLast.getDuration(), head, newLast.prev);
			newLast.prev.next = head.prev;
			head.prev.next = head;
			size--;
			return oldLast;
		}

	}
	//delete an episode with the specified title (ignores word case)
	public Episode deleteEpisode(String title)
	{

		Episode deletedEpisode = null;
		Episode previous = null;
		Episode current = head;

		//first we test corner cases when size == 0; then when the deleted is head or last 
		if (size == 0) {
			throw new RuntimeException("cannot delete from an empty playlist!");
		} else if (head.getTitle().equalsIgnoreCase(title)) {
			deletedEpisode = head;
			deleteFirst();
			return deletedEpisode;

		} else if (head.prev.getTitle().equalsIgnoreCase(title)) {
			deletedEpisode = head.prev;
			deleteLast();
			return deletedEpisode;

		} else {
			while (current.next != head) {
				if (!current.getTitle().equalsIgnoreCase(title)) {
					previous = current;
					current = current.next;  //current should now be pointing at the node that has the title
				}

			} 

			if (current.getTitle().equalsIgnoreCase(title)) { //perform the deletion when found
				deletedEpisode = current;
				Episode theNext = current.next;  
				previous.next = theNext;
				theNext.prev = previous;

				size--;
				return deletedEpisode;
			}
			return deletedEpisode; //return null if episode is not found
		}
	}

	//delete every Mth episode until 1 episode remains and return that episode
	public Episode deleteEveryMthEpisode(int m) {

		Episode current = head;
		Episode previous = null;
		if (size == 0) {    //empty playlist
			throw new RuntimeException("cannot delete from an empty playlist!");
		} else if (size == 1) {   // size of 1 playlist
			return head; 
		} else if (m <= 0) {   // out of bound index
			throw new RuntimeException("please enter a positive integer");
		} else if (m == 1) {     // first Episode condition

			//return head.prev;
			while (size != 1) {
				previous = current.prev;
				Episode deletedOnes = current;
				Episode theNext = current.next;
				previous.next = theNext;
				theNext.prev = previous;
				current = theNext;
				head = current;
				size--;
			}
			return head;
		} else {
			while (size > 1) {
				for (int i = 0; i < m-1; i++) {
					previous = current;
					current = current.next; //current is pointing now at the element that should be deleted
				}

				Episode newHead = current.next;
				previous.next = newHead;
				newHead.prev = previous;
				size--;
				current = newHead;
				head = current;
			}
		}
		return head;

	}



} 
