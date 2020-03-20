import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRevision } from 'app/shared/model/revision.model';
import { RevisionService } from './revision.service';

@Component({
  templateUrl: './revision-delete-dialog.component.html'
})
export class RevisionDeleteDialogComponent {
  revision?: IRevision;

  constructor(protected revisionService: RevisionService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.revisionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('revisionListModification');
      this.activeModal.close();
    });
  }
}
