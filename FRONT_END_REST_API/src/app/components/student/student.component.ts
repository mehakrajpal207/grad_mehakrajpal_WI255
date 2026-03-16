import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StudentService } from '../../services/student.service';
import { Student } from '../../models/student';

@Component({
  selector: 'app-student',
  standalone: true,
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css'],
  imports: [CommonModule, FormsModule]
})
export class StudentComponent implements OnInit {

  students: Student[] = [];

  student: Student = {
    univRegNo: '',
    name: '',
    standard: '',
    school: '',
    gender: '',
    percentage: 0
  };

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    this.loadStudents();
  }

  loadStudents() {
    this.studentService.getStudents().subscribe(data => {
      this.students = data;
    });
  }

  addStudent() {
    this.studentService.addStudent(this.student).subscribe(() => {
      this.loadStudents();
      this.student = {
        univRegNo: '',
        name: '',
        standard: '',
        school: '',
        gender: '',
        percentage: 0
      };
    });
  }

  deleteStudent(id: number | undefined) {
    if(id){
      this.studentService.deleteStudent(id).subscribe(() => {
        this.loadStudents();
      });
    }
  }

}