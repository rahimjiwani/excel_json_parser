import { ParserServiceService } from './parser-service.service';
import { Constants } from './constant';
import { Component, ViewChild, OnInit, ContentChildren, QueryList, ChangeDetectorRef } from '@angular/core';
import { Schema } from './schema';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatTable, MatTableDataSource } from '@angular/material';
import { CdkDragDrop, moveItemInArray, transferArrayItem, CdkDropList } from '@angular/cdk/drag-drop';
import * as filesaver from '../../node_modules/file-saver';
import { MatStepper } from '@angular/material/stepper';
import { ToastrService } from 'ngx-toastr';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  @ViewChild('table', { static: false }) table: MatTable<Schema>;
  // displayedColumns: string[] = ['position', 'name', 'weight', 'symbol'];
  dataSource = [];
  isLinear = true;
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  schemaList: Schema[] = [];
  fileLabel = "Upload JSON"
  fileLabel2 = "Upload Excel"
  displayedColumns: string[] = ['fieldName', 'dataType', 'length', 'isRequired', 'isIgnored'];
  fileContent = null;

  dataTypes = Constants.dataTypes;

  NUMBER_PATTERN = "^[0-9]\d*$";

  constructor(private _formBuilder: FormBuilder,
    private toastr: ToastrService,
    private changeDetectorRef: ChangeDetectorRef,
    private parserService: ParserServiceService) { }

  ngOnInit() {
    this.firstFormGroup = this._formBuilder.group({
      fieldName: ['', Validators.required],
      dataType: ['', Validators.required],
      ignore: [false],
      required: [true],
      length: []
    });
    this.secondFormGroup = this._formBuilder.group({
      secondCtrl: ['', Validators.required]
    });

    this.dataSource = this.schemaList;
  }


  addSchema() {
    if (this.firstFormGroup.valid) {
      let schema: Schema = new Schema();
      schema.fieldName = this.firstFormGroup.controls.fieldName.value.replace(/\s/g, '');;
      schema.dataType = this.firstFormGroup.controls.dataType.value;
      schema.isIgnore = this.firstFormGroup.controls.ignore.value;
      schema.isRequired = this.firstFormGroup.controls.required.value
      schema.length = this.firstFormGroup.controls.length.value
      this.schemaList.push(schema);
      console.log(this.schemaList)
      this.firstFormGroup.reset();
      this.patchValue();

      this.dataSource = this.schemaList;
      this.changeDetectorRef.detectChanges();
      this.table.renderRows();

    }
  }

  patchValue() {
    this.firstFormGroup.patchValue({
      required: true,
      ignore: false
    })
  }

  resetSchemaForm(){
    this.firstFormGroup.reset();
    this.patchValue();
  }

  selectionChange(event) {
    console.log(this.firstFormGroup.controls.dataType.value);
    const length = this.firstFormGroup.get('length')
    if (this.firstFormGroup.controls.dataType.value == 'ALPHANUMERIC') {
      length.setValidators([Validators.required, Validators.pattern(this.NUMBER_PATTERN)]);
      this.firstFormGroup.patchValue({
        length: 1
      })
    } else {
      length.clearValidators();
      this.firstFormGroup.patchValue({
        length: null
      })
    }
    length.updateValueAndValidity();
  }

  dropTable(event: CdkDragDrop<Schema[]>) {
    const prevIndex = this.dataSource.findIndex((d) => d === event.item.data);
    moveItemInArray(this.dataSource, prevIndex, event.currentIndex);
    this.table.renderRows();
  }

  download() {
    let data = JSON.stringify(this.schemaList, null, "    ")
    var blob = new Blob([data], { type: "application/json" });
    filesaver.saveAs(blob, "Schema.json");
  }

  uploadFile(event) {

    this.fileLabel = event.target.files[0].name
    let fileReader = new FileReader();
    let file = event.target.files[0]
    fileReader.onload = (e) => {
      let data: Schema[] = JSON.parse((fileReader.result).toString())
      this.schemaList = data;
      console.log(this.schemaList)
      this.dataSource = this.schemaList;
      this.changeDetectorRef.detectChanges();
    }
    fileReader.readAsText(file);
  }

  uploadFileExcel(event) {
    let fileName = event.target.files[0].name
    let fileExtension = fileName.substr(fileName.lastIndexOf(".") + 1)
    if (Constants.fileFormat.indexOf(fileExtension) != -1) {
      this.fileLabel2 = event.target.files[0].name
      this.fileContent = event.target.files[0];

      let data = JSON.stringify(this.schemaList)
      var blob = new Blob([data], { type: "application/json" });

      const formData = new FormData();
      formData.append('file', this.fileContent);
      formData.append('schema', blob)
      this.parserService.postParse(formData).subscribe(res=> {
        console.log("hiss")
      });
      // this.parserService.getTest().subscribe(res=> {
      //   console.log("hiss")
      // });

    } else {
      this.showError('Please upload xlsx file');
    }
    console.log()

  }

  goNext(stepper: MatStepper, index: number) {
    if ((this.schemaList.length > 0 && index == 0) || (index == 1 && this.fileContent != null)) {
      stepper._steps.toArray()[index].stepControl.status = "valid"
    } else {
      let errorMsg = '';
      if (index == 0) {
        errorMsg = "Please add schema via form or upload"
      } else {
        errorMsg = "Please upload excel file"
      }
      this.showError(errorMsg);
    }
    stepper.next();

  }

  reset(event){
    event.target.value = null;
  }

  showSuccess(msg) {
    this.toastr.success(msg, 'Success');
  }

  showError(msg) {
    this.toastr.error(msg, 'Error!');
  }
}


