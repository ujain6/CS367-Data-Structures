public class UnitTest {

	public static void main(String[] args) {
		// total points taken off
		int total_points_off = 0;

		// processRegistrationList() enroll some fails
		int processRegList_EnrollSomeFail = -5;

		Course course;
		Student s1, s2, s3;
		System.out.println("Start Unit Test..");

		try {
			s1 = new Student("s1", "id1", 100);
			s2 = new Student("s2", "id2", 100);
			s3 = new Student("s3", "id3", 100);
			course = new Course("1", "TestCourse1", 2);
			course.addStudent(s3, 50);
			course.addStudent(s1, 50);
			course.addStudent(s2, 60);
			
			System.out.println("Start processRegistrationList..");
			course.processRegistrationList();
			System.out.println("Finish processRegistrationList..");

			if (course.getCourseRegister().size() != 2
					|| !course.getCourseRegister().get(0).equals(s2)
					|| !course.getCourseRegister().get(1).equals(s3)) {
				System.out
						.println("Unit test - PROBLEM("
								+ processRegList_EnrollSomeFail
								+ " point): roster size should be two, with students in a particular order");
				total_points_off += processRegList_EnrollSomeFail;
			}
		} catch (Exception ex) {
			System.out
					.println("Unit test - PROBLEM("
							+ processRegList_EnrollSomeFail
							+ " point): throws unexpected exceptions when test Course..");
			total_points_off += processRegList_EnrollSomeFail;
		}
	}

}
