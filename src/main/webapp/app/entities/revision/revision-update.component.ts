import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IRevision, Revision } from 'app/shared/model/revision.model';
import { RevisionService } from './revision.service';
import { IDocument } from 'app/shared/model/document.model';
import { DocumentService } from 'app/entities/document/document.service';

@Component({
  selector: 'jhi-revision-update',
  templateUrl: './revision-update.component.html'
})
export class RevisionUpdateComponent implements OnInit {
  isSaving = false;
  documents: IDocument[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    summary: [],
    reviewer: [],
    document: []
  });

  constructor(
    protected revisionService: RevisionService,
    protected documentService: DocumentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ revision }) => {
      this.updateForm(revision);

      this.documentService.query().subscribe((res: HttpResponse<IDocument[]>) => (this.documents = res.body || []));
    });
  }

  updateForm(revision: IRevision): void {
    this.editForm.patchValue({
      id: revision.id,
      title: revision.title,
      summary: revision.summary,
      reviewer: revision.reviewer,
      document: revision.document
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const revision = this.createFromForm();
    if (revision.id !== undefined) {
      this.subscribeToSaveResponse(this.revisionService.update(revision));
    } else {
      this.subscribeToSaveResponse(this.revisionService.create(revision));
    }
  }

  private createFromForm(): IRevision {
    return {
      ...new Revision(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      summary: this.editForm.get(['summary'])!.value,
      reviewer: this.editForm.get(['reviewer'])!.value,
      document: this.editForm.get(['document'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRevision>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IDocument): any {
    return item.id;
  }
}
